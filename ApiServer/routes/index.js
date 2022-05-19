const express = require('express');
const router = express.Router();
const controller = require('../controller/controller')

/* GET home page. */
router.get('/', controller.getData);
router.post('/addRecordMedical', controller.postData)
router.post('/addNewRecord', controller.addNewRecord)
router.get("/getDataNewRecord", controller.getDataNewRecord)
router.get("/getDiseases", controller.getDiseases)
router.get("/getMedicine", controller.getMedicine)
router.post("/addAccount", controller.addAccount)
router.get("/getAccount", controller.getAccount)
router.post("/addMedicine", controller.addMedicine)
router.post("/addDiseases", controller.addDiseases)
router.post("/addDetailMedicine", controller.addDetailMedicine)
router.get("/getDetailMedicine", controller.getDetailMedicine)
router.post("/editDone", controller.editDone)
router.get("/getAccountFromId", controller.getAccountFromId)
router.get("/getDiseasesOfWebsite", controller.getDiseasesOfWebsite)
router.get("/getDataFromWebsite1", controller.getDataFromWebSite1)
router.get("/getListOfDiseasesFromAlphabet", controller.getListOfDiseasesFromAlphabet)
router.get("/getTotalDrugFromDiseases", controller.getTotalDrugFromDiseases)
router.get("/iTest", controller.iTest)
router.get("/getMedicineOfWebSite", controller.getInformationDrugs)
router.get("/getListOfDrugsFromAlphabet", controller.getListOfDrugsFromAlphabet)

module.exports = router;
