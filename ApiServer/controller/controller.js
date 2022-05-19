const connection = require('../db')
const util = require('util')
const query = util.promisify(connection.query).bind(connection)
const cheerio = require('cheerio') // khai báo module cheerio
const request = require('request-promise')// khai báo module request-prom
const fs = require('fs')

const postData = async (req, res) => {
    try {
        const {idUser, date, time, diseases, medicine, nameDoctor, numberPhone} = req.query
        const postData = await query(`INSERT INTO table3(id, id_of_user, date, time, diseases, medicine, name_doctor,
                                                         number_phone_of_doctor)
                                      VALUES (null, '${idUser}', '${date}', '${time}', '${diseases}', '${medicine}',
                                              '${nameDoctor}',
                                              '${numberPhone}')`);
        res.json({
            msg: 'ok'
        })
    } catch (e) {
        console.error(e)
    }
}

const getData = async (req, res) => {
    try {
        const {id_user} = req.query
        const getData = await query(`SELECT *
                                     FROM table3
                                     WHERE id_of_user = '${id_user}'`)
        const data = await query(`SELECT * FROM table_accounts`)
        res.json({
            resultMedical: getData,
            resultAccounts : data
        })
    } catch (e) {
        console.error(e)
    }
}

const addNewRecord = async (req, res) => {
    try {
        const {id_user, name, birth_day, sex, height, weight} = req.query
        const addNewRecord = await query(`INSERT INTO new_record(id, id_of_user, name, birth_day, sex, height, weight)
                                          VALUES (null, '${id_user}', '${name}', '${birth_day}', '${sex}', '${height}',
                                                  '${weight}')`)
    } catch (e) {
        console.error(e)
    }
}

const getDataNewRecord = async (req, res) => {
    try {
        const {id_user} = req.query
        const getDataNewRecord = await query(`SELECT *
                                              FROM new_record
                                              WHERE id_of_user = '${id_user}'`)
        res.json({
            resultNewRecord: getDataNewRecord
        })
    } catch (e) {
        console.error(e)
    }
}

const getDiseases = async (req, res) => {
    try {
        const {id_user} = req.query
        const getData = await query(`SELECT *
                                     FROM table_diseases
                                     WHERE id_of_user = '${id_user}'`)
        res.json({
            resultDiseases: getData
        })
    } catch (e) {
        console.log(e)
    }
}

const getMedicine = async (req, res) => {
    try {
        const {id_user} = req.query
        console.log(id_user)
        const getData = await query(`SELECT *
                                     FROM table_medicine
                                     WHERE id_of_user = '${id_user}'`)
        res.json({
            resultMedicine: getData
        })
    } catch (e) {
        console.log(e)
    }
}

const addAccount = async (req, res) => {
    try {
        const {username, password, done} = req.query
        const addAccount = await query(`INSERT INTO table_accounts (id, username, password, done)
                                        VALUES (null, '${username}', '${password}', '${done}')`)
    } catch (e) {

    }
}

const getAccount = async (req, res) => {
    try {
        const getData = await query(`SELECT *
                                     FROM table_accounts`)
        res.json({
            resultAccount: getData
        })
    } catch (e) {
        console.log(e)
    }
}

const addMedicine = async (req, res) => {
    try {
        const {id_user, medicine} = req.query
        const addMedicine = await query(`INSERT INTO table_medicine (id, id_of_user, name_medicine)
                                         VALUES (null, '${id_user}', '${medicine}')`)
    } catch (e) {
        console.log(e)
    }
}

const addDiseases = async (req, res) => {
    try {
        const {id_user, diseases} = req.query
        const addDiseases = await query(`INSERT INTO table_diseases (id, id_of_user, name_diseases)
                                         VALUES (null, '${id_user}', '${diseases}')`)
    } catch (e) {
        console.log(e)
    }
}

const addDetailMedicine = async (req, res) => {
    try {
        const {id_user, id_record, name_medicine, number_medicine, time_medicine, notice_medicine} = req.query
        const addDetailMedicine = await query(`INSERT INTO table_detail_medicine (id, id_of_user, id_of_record,
                                                                                  name_medicine, number_medicine,
                                                                                  time_medicine, notice_medicine)
                                               VALUES (null, '${id_user}', '${id_record}', '${name_medicine}',
                                                       '${number_medicine}', '${time_medicine}', '${notice_medicine}')`)
    } catch (e) {
        console.log(e)
    }
}

const getDetailMedicine = async (req, res) => {
    try {
        const {id_user, id_record} = req.query
        const getData = await query(`SELECT *
                                     FROM table_detail_medicine
                                     WHERE id_of_user = '${id_user}'
                                       AND id_of_record = '${id_record}'`)
        res.json({
            resultDetailMedicine: getData
        })
    } catch (e) {
        console.log(e)
    }
}

const editDone = async (req, res) => {
    try {
        const {id_user, done} = req.query
        const editDone = await query(`UPDATE table_accounts
                                      SET done = '${done}'
                                      WHERE id = '${id_user}'`)
    } catch (e) {
        console.log(e)
    }
}

const getAccountFromId = async (req, res) => {
    try {
        const {id_user} = req.query
        const getData = await query(`SELECT *
                                     FROM table_accounts
                                     WHERE id = '${id_user}'`)
        res.json({
            resultAccount: getData
        })
    } catch (e) {
        console.log(e)
    }
}

const getDiseasesOfWebsite = async (req, res) => {
    let data = []
    let name_diseases = null
    let url_of_diseases = null

    request({
        url: 'https://www.cdc.gov/az/a.html?CDC_AA_refVal=https%3A%2F%2Fwww.cdc.gov%2Faz%2Findex.html', headers: {
            'User-Agent': 'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:15.0) Gecko/20100101 Firefox/15.0.1'
        }
    }, (error, response, html) => { // gửi request đến trang
        if (!error && response.statusCode === 200) {
            const $ = cheerio.load(html);
            $('.az-strip a').each((index, element) => {
                name_diseases = $(element).find('div').text()
                url_of_diseases = $(element).attr('href')
                data.push({
                    name_diseases, url_of_diseases
                })
            })
            fs.writeFileSync('data.json', JSON.stringify(data)); // lưu dữ liệu vào file data.json
            res.json({
                resultDiseases: data
            })
        } else {
            console.log(error);
        }
    });
}

const getDataFromWebSite1 = async (req, res) => {
    let data = []
    let name_medicine = null
    request({
        url: 'https://www.rxlist.com/drugs/alpha_a.htm', headers: {
            'User-Agent': 'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:15.0) Gecko/20100101 Firefox/15.0.1'
        }
    }, (error, response, html) => { // gửi request đến trang
        if (!error && response.statusCode === 200) {
            const $ = cheerio.load(html);
            $('.w-full #A_Z ul li').each((index, element) => { // lặp từng phần tử có class là job__list-item
                name_medicine = $(element).find('a').text();
                data.push({
                    name_medicine
                });
            })
            fs.writeFileSync('data.json', JSON.stringify(data)); // lưu dữ liệu vào file data.json
            res.json({
                resultMedicine: data
            })
        } else {
            console.log(error);
        }
    });
}

const getListOfDiseasesFromAlphabet = async (req, res) => {
    const {url} = req.query
    let data = []
    let name_diseases = null
    let url_drug_of_diseases = null
    request({
        url: `https://www.cdc.gov${url}`, headers: {
            'User-Agent': 'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:15.0) Gecko/20100101 Firefox/15.0.1'
        }
    }, (error, response, html) => { // gửi request đến trang
        if (!error && response.statusCode === 200) {
            const $ = cheerio.load(html);
            $('.az-content ul li').each((index, element) => {
                name_diseases = $(element).find('a').text()
                url_drug_of_diseases = $(element).find('a').attr('href')
                data.push({
                    name_diseases, url_drug_of_diseases
                })
            })
            let i
            for (i = 1; i <= 5; i++) {
                data.shift()
            }
            for (i = 1; i <= 26; i++) {
                data.pop()
            }
            fs.writeFileSync('data.json', JSON.stringify(data));
            res.json({
                resultDiseases: data
            })
        } else {
            console.log(error);
        }
    });
}

//a
const getTotalDrugFromDiseases = async (req, res) => {
    // const {url} = req.query
    const url = '/drugs/alpha_b.htm'
    let data = []
    let name_medicine = null
    let url_drug_of_diseases = null
    request({
        url: `https://www.rxlist.com${url}`, headers: {
            'User-Agent': 'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:15.0) Gecko/20100101 Firefox/15.0.1'
        }
    }, (error, response, html) => { // gửi request đến trang
        if (!error && response.statusCode === 200) {
            const $ = cheerio.load(html);
            $('.AZ_results ul li').each((index, element) => { // lặp từng phần tử có class là job__list-item
                name_medicine = $(element).find('a').text();
                url_drug_of_diseases = $(element).find('a').attr('href');
                data.push({
                    name_medicine, url_drug_of_diseases
                });
            })
            fs.writeFileSync('data.json', JSON.stringify(data)); // lưu dữ liệu vào file data.json
            res.json({
                resultMedicine: data
            })
        } else {
            console.log(error);
        }
    });
}

const iTest = async (req, res) => {
    let data = []
    let html_ = null
    request({
        url: 'https://www.drugs.com/mtm/sodium-bicarbonate.html', headers: {
            'User-Agent': 'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:15.0) Gecko/20100101 Firefox/15.0.1'
        }
    }, (error, response, html) => { // gửi request đến trang
        if (!error && response.statusCode === 200) {
            const $ = cheerio.load(html);
            $('.container').each((index, element) => { // lặp từng phần tử có class là job__list-item
                html_ = $(element).find('.ddc-clear-both div').html();
                data.push({
                    html_
                });
            })
            // fs.writeFileSync('data.json', JSON.stringify(data)); // lưu dữ liệu vào file data.json
            res.json({
                resultHtml: data
            })
        } else {
            console.log(error);
        }
    });
}

const getInformationDrugs = async (req, res) => {
    let data = []
    let name_medicine = null
    let url_drug_of_diseases = null
    request({
        url: 'https://www.rxlist.com/drugs/alpha_a.htm', headers: {
            'User-Agent': 'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:15.0) Gecko/20100101 Firefox/15.0.1'
        }
    }, (error, response, html) => { // gửi request đến trang
        if (!error && response.statusCode === 200) {
            const $ = cheerio.load(html);
            $('.w-full #A_Z ul li').each((index, element) => { // lặp từng phần tử có class là job__list-item
                name_medicine = $(element).find('a').text();
                url_drug_of_diseases = $(element).find('a').attr('href');
                data.push({
                    name_medicine, url_drug_of_diseases
                });
            })
            // fs.writeFileSync('data.json', JSON.stringify(data)); // lưu dữ liệu vào file data.json
            res.json({
                resultMedicine: data
            })
        } else {
            console.log(error);
        }
    });
}

const getListOfDrugsFromAlphabet = async (req, res)=>{
    const {url} = req.query
    let data = []
    let name_medicine = null
    let url_drug_of_diseases = null
    request({
        url: `https://www.rxlist.com${url}`, headers: {
            'User-Agent': 'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:15.0) Gecko/20100101 Firefox/15.0.1'
        }
    }, (error, response, html) => { // gửi request đến trang
        if (!error && response.statusCode === 200) {
            const $ = cheerio.load(html);
            $('.AZ_results ul li').each((index, element) => { // lặp từng phần tử có class là job__list-item
                name_medicine = $(element).find('a').text();
                url_drug_of_diseases = $(element).find('a').attr('href');
                data.push({
                    name_medicine, url_drug_of_diseases
                });
            })
            // fs.writeFileSync('data.json', JSON.stringify(data)); // lưu dữ liệu vào file data.json
            res.json({
                resultMedicine: data
            })
        } else {
            console.log(error);
        }
    });
}
module.exports = {
    getData, postData, addNewRecord, getDataNewRecord, getDiseases, getMedicine, addAccount,
    getAccount, addMedicine, addDiseases, addDetailMedicine, getDetailMedicine, editDone, getAccountFromId,
    getDiseasesOfWebsite, getDataFromWebSite1, getListOfDiseasesFromAlphabet, getTotalDrugFromDiseases, iTest,
    getInformationDrugs, getListOfDrugsFromAlphabet
}