package ir.khalili.products.nas.core.verticle.request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import ir.khalili.products.nas.core.constants.AppConstants;
import ir.khalili.products.nas.core.dao.DAO_CustomerService;
import ir.khalili.products.nas.core.dao.DAO_Log;
import ir.khalili.products.nas.core.dto.CreditReportNaturalPersonDto;
import ir.khalili.products.nas.core.excp.dao.DAOEXCP_Internal;
import ir.khalili.products.nas.core.helper.HelperConnection;
import ir.khalili.products.nas.core.utils.Base64Convertor;
import ir.khalili.products.nas.core.utils.Configuration;
import ir.khalili.products.nas.core.utils.excp.WSEXCP_IOException;
import ir.khalili.products.nas.core.utils.pdf.CreditReportNaturalPerson;


public class VRTCL_16_DownloadCreditReportNaturalPerson extends AbstractVerticle {

    private static final Logger logger = LogManager.getLogger(VRTCL_16_DownloadCreditReportNaturalPerson.class);
    private final String address = AppConstants.EVNT_BUS_ADR_SRVCS_REQ_CREDIT_REPORT_NATURAL_PERSON;

    @Override
    public void start() throws Exception {

        logger.info("Starting verticle " + this.getClass().getSimpleName());

        Configuration.config = config();

        vertx.eventBus().consumer(address , message -> {

            logger.trace("Event " + address + " recieved with message:" + ((JsonObject) (message.body())));

            new HelperConnection() {
                @Override
                public void callBiz(SQLConnection sqlConnection , Handler<AsyncResult<JsonObject>> resultHandler) {
                    creditReportNaturalPerson(vertx , sqlConnection , (JsonObject) (message.body()) , resultHandler);
                }
            }.getTransactionalConnection(vertx , this.getClass().getSimpleName() , message);

        });

    }

    private static void creditReportNaturalPerson(Vertx vertx , SQLConnection sqlConnection , JsonObject message , Handler<AsyncResult<JsonObject>> resultHandler) {
        logger.info("Start request undertake: {}" , message);

        final long customerServiceId = message.getLong("customerServiceId");

        DAO_CustomerService.fetchCustomerServiceById(sqlConnection , customerServiceId).onSuccess(customerService -> {

            logger.info("Successfully customer service id: {}" , customerService);

            DAO_Log.fetchOutput(sqlConnection , customerServiceId , "inqueryIranianDetails").onSuccess(log -> {

                logger.info("Successfully fetch log by customer service id: {}" , log);

                if (log.isEmpty()) {
                    logger.info("Not found log");
                    resultHandler.handle(Future.succeededFuture(
                            new JsonObject()
                                    .put("resultCode" , 0)
                                    .put("resultMessage" , "موردی جهت ساخت گزارش یافت نشد.")
                                    .put("info" , Base64Convertor.createEmptyInfoFileJson())
                    ));
                    return;
                }

                final JsonArray inquiryDetails;
                try {
                    //                    final JsonObject joIranian = new JsonObject("{\"inquiryDetailsResponseDtos\":[{\"type\":\"IRANIAN_BASE\",\"result\":{\"data\":{\"personInformation\":{\"dateOfBirth\":\"1375/07/11\",\"family\":\"ذكريائي\",\"fatherName\":\"ميراكبر\",\"maritalStatus\":\"مجرد\",\"maritalStatusCode\":\"0106333\",\"placeOfBirth\":\"ارومیه\",\"placeOfBirthCode\":\"0105100133\",\"sex\":\"زن\",\"sexCode\":\"0109333\",\"name\":\"مائده\",\"nationalCode\":\"2741184062\",\"address\":\"نامشخص\",\"contacts\":[{\"title\":\"تلفن ثابت\",\"value\":\" 04413362235-09033592851 \"},{\"title\":\"تلفن ثابت\",\"value\":\" - \"}],\"hasBlockedContract\":false},\"inquiryStats\":[{\"organizationType\":\"بانک مرکزی\",\"organizationTypeCode\":\"0102533\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":0,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0},{\"organizationType\":\"بانک ها و موسسات اعتباری\",\"organizationTypeCode\":\"010333\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":1,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0},{\"organizationType\":\"تلکام/سازمان هاي خدماتی\",\"organizationTypeCode\":\"0108933\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":0,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0},{\"organizationType\":\"سازمان های خرده فروشی\",\"organizationTypeCode\":\"0106633\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":0,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0},{\"organizationType\":\"سازمان های عمومی/دولتی\",\"organizationTypeCode\":\"01010233\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":0,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0},{\"organizationType\":\"سازمان های قضايی\",\"organizationTypeCode\":\"01012233\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":0,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0},{\"organizationType\":\"مصرف کنندگان\",\"organizationTypeCode\":\"01010033\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":0,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0},{\"organizationType\":\"موسسات اعتباري غير متشکل\",\"organizationTypeCode\":\"0103033\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":0,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0},{\"organizationType\":\"موسسات صادر کننده کارت اعتباری\",\"organizationTypeCode\":\"0104433\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":0,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0}],\"personRoles\":[{\"title\":\"متقاضی اصلی\",\"numberOfContracts\":0,\"code\":\"0101333\"},{\"title\":\"ضامن\",\"numberOfContracts\":0,\"code\":\"0109433\"}],\"summaryInformationRelatedToContracts\":[{\"organizationName\":\"بانک ملت\",\"organizationNameCode\":\"0201271\",\"currency\":\"ریال\",\"currencyCode\":\"0102933\",\"inProgressContractsCount\":0,\"terminatedContractsCount\":1,\"outstandingAmount\":0.0,\"overdueAmount\":0.0,\"typeOfFinancing\":\"تسهیلات\",\"typeOfFinancingCode\":\"010233\"}],\"summaryInformationRelatedToContractsSum\":[{\"currency\":\"ریال\",\"currencyCode\":\"0102933\",\"outstandingAmount\":0.0,\"overdueAmount\":0.0,\"inProgressContractsCount\":0,\"terminatedContractsCount\":1}],\"inquiryCreatedDate\":\"1402/03/07 17:38:43\",\"reportUniqueId\":\"4000 4080 3000 0530 8706\"},\"message\":{},\"messageList\":[],\"total\":null,\"status\":200},\"inquiryStatus\":\"DONE\",\"responseTime\":0},{\"type\":\"IRANIAN_STANDARD\",\"result\":{\"data\":{\"directContracts\":[{\"organizationName\":\"بانک ملت\",\"organizationNameCode\":\"0101233\",\"organizationType\":\"بانک ها و موسسات اعتباری\",\"organizationTypeCode\":\"010333\",\"contractNumber\":\"9585267864\",\"contractCurrency\":\"ریال\",\"contractCurrencyCode\":\"0102933\",\"installmentsCurrency\":\"ریال\",\"installmentsCurrencyCode\":\"0102933\",\"customerRoleTilte\":\"متقاضی اصلی\",\"customerRole\":13,\"customerRoleCode\":\"0101333\",\"expectedEndDate\":\"1398/07/02\",\"instalmentAmount\":1199093.0,\"paymentMethod\":\"حساب جاری\",\"paymentMethodCode\":\"01011133\",\"negativeContractStatus\":\"فاقد وضعیت منفی\",\"negativeContractStatusCode\":\"0104033\",\"outstandingAmount\":0.0,\"overdueAmount\":0.0,\"paymentPeriodicity\":\"پرداخت در پایان قرارداد\",\"paymentPeriodicityCode\":\"0104633\",\"phaseOfOperationTitle\":\"خاتمه یافته عادی طبق قرارداد\",\"phaseOfOperation\":112,\"phaseOfOperationCode\":\"01011233\",\"purposeOfTheCredit\":\"سایر\",\"purposeOfTheCreditCode\":\"0107933\",\"accountingDate\":\"1398/08/29\",\"startDate\":\"1395/06/15\",\"totalAmount\":196554442.0,\"typeOfFinancingTitle\":\"سایر\",\"typeOfFinancing\":2,\"typeOfFinancingCode\":\"010233\",\"typeofInstallments\":\"اقساط ثابت\",\"typeofInstallmentsCode\":\"0105733\",\"realEndDate\":\"1398/08/20\",\"totalNumberOfInstallments\":36,\"totalNumberOfOverdue\":0,\"totalNumberOfOutstanding\":0,\"guarantees\":[{\"value\":150000000.0,\"type\":\"سایر\",\"typeCode\":null,\"currency\":\"ریال\",\"currencyCode\":null}],\"relatedPersons\":[],\"repayments\":[],\"contractStartDateIsTransactionType\":false}],\"contractNegativeStatus\":[{\"customerRole\":13,\"customerRoleCode\":\"0101333\",\"contractCode\":\"9585267864\",\"organizationName\":\"بانک ملت\",\"organizationNameCode\":\"0101233\",\"organizationType\":\"بانک ها و موسسات اعتباری\",\"organizationTypeCode\":\"010333\",\"date\":\"1398/05/02\",\"state\":\"سررسید گذشته\",\"stateCode\":\"0106133\"},{\"customerRole\":13,\"customerRoleCode\":\"0101333\",\"contractCode\":\"9585267864\",\"organizationName\":\"بانک ملت\",\"organizationNameCode\":\"0101233\",\"organizationType\":\"بانک ها و موسسات اعتباری\",\"organizationTypeCode\":\"010333\",\"date\":\"1397/02/30\",\"state\":\"سررسید گذشته\",\"stateCode\":\"0106133\"},{\"customerRole\":13,\"customerRoleCode\":\"0101333\",\"contractCode\":\"9585267864\",\"organizationName\":\"بانک ملت\",\"organizationNameCode\":\"0101233\",\"organizationType\":\"بانک ها و موسسات اعتباری\",\"organizationTypeCode\":\"010333\",\"date\":\"1397/02/21\",\"state\":\"سررسید گذشته\",\"stateCode\":\"0106133\"},{\"customerRole\":13,\"customerRoleCode\":\"0101333\",\"contractCode\":\"9585267864\",\"organizationName\":\"بانک ملت\",\"organizationNameCode\":\"0101233\",\"organizationType\":\"بانک ها و موسسات اعتباری\",\"organizationTypeCode\":\"010333\",\"date\":\"1396/09/29\",\"state\":\"سررسید گذشته\",\"stateCode\":\"0106133\"},{\"customerRole\":13,\"customerRoleCode\":\"0101333\",\"contractCode\":\"9585267864\",\"organizationName\":\"بانک ملت\",\"organizationNameCode\":\"0101233\",\"organizationType\":\"بانک ها و موسسات اعتباری\",\"organizationTypeCode\":\"010333\",\"date\":\"1396/09/14\",\"state\":\"سررسید گذشته\",\"stateCode\":\"0106133\"}],\"personNegativeStatus\":[{\"contractCode\":null,\"organizationName\":\"بانک ملت\",\"organizationNameCode\":\"0101233\",\"organizationType\":\"بانک ها و موسسات اعتباری\",\"organizationTypeCode\":\"010333\",\"date\":\"1397/06/14\",\"state\":\"فاقد وضعیت منفی\",\"stateCode\":\"01015633\"}],\"personInformation\":{\"dateOfBirth\":\"1375/07/11\",\"family\":\"ذكريائي\",\"fatherName\":\"ميراكبر\",\"maritalStatus\":\"مجرد\",\"maritalStatusCode\":\"0106333\",\"placeOfBirth\":\"ارومیه\",\"placeOfBirthCode\":\"0105100133\",\"sex\":\"زن\",\"sexCode\":\"0109333\",\"name\":\"مائده\",\"nationalCode\":\"2741184062\",\"address\":\"نامشخص\",\"contacts\":[{\"title\":\"تلفن ثابت\",\"value\":\" 04413362235-09033592851 \"},{\"title\":\"تلفن ثابت\",\"value\":\" - \"}],\"hasBlockedContract\":false},\"inquiryStats\":[{\"organizationType\":\"بانک مرکزی\",\"organizationTypeCode\":\"0102533\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":0,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0},{\"organizationType\":\"بانک ها و موسسات اعتباری\",\"organizationTypeCode\":\"010333\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":1,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0},{\"organizationType\":\"تلکام/سازمان هاي خدماتی\",\"organizationTypeCode\":\"0108933\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":0,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0},{\"organizationType\":\"سازمان های خرده فروشی\",\"organizationTypeCode\":\"0106633\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":0,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0},{\"organizationType\":\"سازمان های عمومی/دولتی\",\"organizationTypeCode\":\"01010233\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":0,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0},{\"organizationType\":\"سازمان های قضايی\",\"organizationTypeCode\":\"01012233\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":0,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0},{\"organizationType\":\"مصرف کنندگان\",\"organizationTypeCode\":\"01010033\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":0,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0},{\"organizationType\":\"موسسات اعتباري غير متشکل\",\"organizationTypeCode\":\"0103033\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":0,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0},{\"organizationType\":\"موسسات صادر کننده کارت اعتباری\",\"organizationTypeCode\":\"0104433\",\"lastMonthInquiryCount\":0,\"lastYearInquiryCount\":0,\"personNegativeStatusCount\":0,\"contractNegativeStatusCount\":0}],\"personRoles\":[{\"title\":\"متقاضی اصلی\",\"numberOfContracts\":0,\"code\":\"0101333\"},{\"title\":\"ضامن\",\"numberOfContracts\":0,\"code\":\"0109433\"}],\"summaryInformationRelatedToContracts\":[{\"organizationName\":\"بانک ملت\",\"organizationNameCode\":\"0201271\",\"currency\":\"ریال\",\"currencyCode\":\"0102933\",\"inProgressContractsCount\":0,\"terminatedContractsCount\":1,\"outstandingAmount\":0.0,\"overdueAmount\":0.0,\"typeOfFinancing\":\"تسهیلات\",\"typeOfFinancingCode\":\"010233\"}],\"summaryInformationRelatedToContractsSum\":[{\"currency\":\"ریال\",\"currencyCode\":\"0102933\",\"outstandingAmount\":0.0,\"overdueAmount\":0.0,\"inProgressContractsCount\":0,\"terminatedContractsCount\":1}],\"inquiryCreatedDate\":\"1402/03/07 17:37:29\",\"reportUniqueId\":\"4000 4080 3000 0533 8604\"},\"message\":{},\"messageList\":[],\"total\":null,\"status\":200},\"inquiryStatus\":\"DONE\",\"responseTime\":0},{\"type\":\"IRANIAN_ADVANCE\",\"result\":{\"data\":null,\"message\":{\"5b0200ef-7878-40e9-b19d-0d5817c94278\":\"اطلاعات موجود برای تولید این گزارش ناکافی است\"},\"messageList\":[\"اطلاعات موجود برای تولید این گزارش ناکافی است\"],\"total\":null,\"status\":404},\"inquiryStatus\":\"DONE\",\"responseTime\":0},{\"type\":\"IRANIAN_SCORE\",\"result\":{\"data\":{\"personInformation\":{\"dateOfBirth\":\"1375/07/11\",\"family\":\"ذكريائي\",\"fatherName\":\"ميراكبر\",\"maritalStatus\":\"مجرد\",\"maritalStatusCode\":\"0106333\",\"placeOfBirth\":\"ارومیه\",\"placeOfBirthCode\":\"0105100133\",\"sex\":\"زن\",\"sexCode\":\"0109333\",\"name\":\"مائده\",\"nationalCode\":\"2741184062\",\"address\":\"نامشخص\",\"contacts\":[{\"title\":\"تلفن ثابت\",\"value\":\" 04413362235-09033592851 \"},{\"title\":\"تلفن ثابت\",\"value\":\" - \"}],\"hasBlockedContract\":false},\"scoreCodes\":[{\"code\":\"NMG2\",\"description\":\"پایین بودنِ تعداد ماههای دارای پرداخت خوب در حداقل یک قرارداد\"},{\"code\":\"AGE1\",\"description\":\"گروه سنی جوان\"},{\"code\":\"HAD2\",\"description\":\"مبلغ بدهی سررسیدشده پرداخت نشده بیش از 10 میلیون ریال در 5 ماه آخر می باشد\u202C\u202C\"},{\"code\":\"RNE3\",\"description\":\"دارای وضعیت منفی قرارداد در 12 ماه گذشته\"}],\"score\":624,\"perviouScore\":true,\"perviouScoreDate\":\"1398/08/29\",\"description\":\"  ریسک کم\",\"risk\":\"B1\",\"inquiryCreatedDate\":\"1402/03/07 17:37:05\",\"reportUniqueId\":\"2000 4080 3000 0534 8607\"},\"message\":{},\"messageList\":[],\"total\":null,\"status\":200},\"inquiryStatus\":\"DONE\",\"responseTime\":0}],\"inquiryID\":\"2741184062-20230521\"}");
                    final JsonObject joIranian = new JsonObject(URLDecoder.decode(log.getString("OUTPUT_JSON2") , StandardCharsets.UTF_8));
                    inquiryDetails = joIranian.getJsonArray("inquiryDetailsResponseDtos");
                } catch (Exception e) {
                    logger.error("Fail to decode iranian info" , e);
                    resultHandler.handle(Future.failedFuture(new DAOEXCP_Internal(-100 , "خطای داخلی. با راهبر سامانه تماس بگیرید.")));
                    return;
                }

                logger.info("Successfully fetch inquiry details: {}" , inquiryDetails);

                if (inquiryDetails == null || inquiryDetails.isEmpty()) {
                    logger.info("inquiry details is empty");
                    resultHandler.handle(Future.succeededFuture(
                            new JsonObject()
                                    .put("resultCode" , -1)
                                    .put("resultMessage" , "موردی جهت ساخت گزارش یافت نشد.")
                                    .put("info" , Base64Convertor.createEmptyInfoFileJson())
                    ));
                    return;
                }

                final JsonObject report;
                try {
                    report = createReport(inquiryDetails);
//                    Base64Convertor.toFile(report.getString(Base64Convertor.KEY_RESULT_FILE) , new File(System.getProperty("user.dir")) , "report.pdf");
                } catch (Exception e) {
                    logger.error("Fail to create report" , e);
                    resultHandler.handle(Future.failedFuture(new DAOEXCP_Internal(-100 , "در ساخت فایل گزارش خطایی رخ داد.")));
                    return;
                }

                resultHandler.handle(Future.succeededFuture(
                        new JsonObject()
                                .put("resultCode" , 1)
                                .put("resultMessage" , "عملیات با موفیت انجام شد.")
                                .put("info" , new JsonObject().put("report" , report))
                ));

            }).onFailure(fail -> { // onFailure for fetchLogByCustomerServiceId
                logger.error("Fail to fetch log by customer service id: {}" , customerServiceId , fail);
                resultHandler.handle(Future.failedFuture(fail));
            });

        }).onFailure(fail -> { // onFailure for fetchCustomerServiceById
            logger.error("Fail to fetch customer service by id: {}" , customerServiceId , fail);
            resultHandler.handle(Future.failedFuture(fail));
        });
    }

    private static JsonObject createReport(final JsonArray joInquiryDetails) throws WSEXCP_IOException {

        try {
            return CreditReportNaturalPerson.create(createDto(joInquiryDetails)).getBase64Pdf();
        } catch (Exception e) {
            logger.error("Fail to create pdf" , e);
            throw new WSEXCP_IOException("خطایی در تولید گزارش رخ داد ، با راهبر سامانه تماس بگیرید");
        }

    }

    private static CreditReportNaturalPersonDto createDto(final JsonArray inquiryDetails) throws Exception {

        final CreditReportNaturalPersonDto dto = CreditReportNaturalPersonDto.dto();

        final JsonObject iranianScore = getJsonByType(inquiryDetails , "IRANIAN_SCORE");

        if (iranianScore != null && !iranianScore.isEmpty()) {
            final String contact = (iranianScore.getJsonObject("personInformation").getJsonArray("contacts") != null && !iranianScore.getJsonObject("personInformation").getJsonArray("contacts").isEmpty()) ?
                    iranianScore.getJsonObject("personInformation").getJsonArray("contacts").getJsonObject(0).getString("value") : "";

            dto.setDatePreparationOfTheReport(iranianScore.getString("inquiryCreatedDate"));

            dto.personal()
                    .firstName(iranianScore.getJsonObject("personInformation").getString("name"))
                    .lastName(iranianScore.getJsonObject("personInformation").getString("family"))
                    .fatherName(iranianScore.getJsonObject("personInformation").getString("fatherName"))
                    .dateOfBirth(iranianScore.getJsonObject("personInformation").getString("dateOfBirth"))
                    .sex(iranianScore.getJsonObject("personInformation").getString("sex"))
                    .nationalCode(iranianScore.getJsonObject("personInformation").getString("nationalCode"))
                    .placeOfBirth(iranianScore.getJsonObject("personInformation").getString("placeOfBirth"))
                    .maritalStatus(iranianScore.getJsonObject("personInformation").getString("maritalStatus"))
                    .address(iranianScore.getJsonObject("personInformation").getString("address"))
                    .landlinePhone(contact);

            dto.setIranianScoreScore(iranianScore.getInteger("score"))
                    .setIranianScoreRisk(iranianScore.getString("risk"))
                    .setIranianScoreDescription(iranianScore.getString("description"));

            final JsonArray scoreCodes = iranianScore.getJsonArray("scoreCodes");
            for (final Object scoreCodeObj : scoreCodes) {
                final JsonObject scoreCode = (JsonObject) scoreCodeObj;
                dto.reasonsForPoints().code(scoreCode.getString("code")).description(scoreCode.getString("description"));
            }
        }

        final JsonObject iranianBase = getJsonByType(inquiryDetails , "IRANIAN_BASE");

        if (iranianBase != null && !iranianBase.isEmpty()) {
            final JsonArray inquiryStats = iranianBase.getJsonArray("inquiryStats");

            if (inquiryStats != null && !inquiryStats.isEmpty()) {
                for (final Object itemObj : inquiryStats) {
                    final JsonObject item = (JsonObject) itemObj;
                    dto.inquiryStatistics()
                            .memberType(item.getString("organizationType"))
                            .lastMonthInquiryCount(item.getInteger("lastMonthInquiryCount"))
                            .lastYearInquiryCount(item.getInteger("lastYearInquiryCount"))
                            .personNegativeStatusCount(item.getInteger("personNegativeStatusCount"))
                            .contractNegativeStatusCount(item.getInteger("contractNegativeStatusCount"));
                }
            }

            final JsonArray summaryInformationRelatedToContracts = iranianBase.getJsonArray("summaryInformationRelatedToContracts");

            if (summaryInformationRelatedToContracts != null && !summaryInformationRelatedToContracts.isEmpty()) {
                for (Object itemObj : summaryInformationRelatedToContracts) {
                    final JsonObject item = (JsonObject) itemObj;
                    dto.contractInformation()
                            .truster(item.getString("organizationName"))
                            .contractType(item.getString("typeOfFinancing"))
                            .currency(item.getString("currency"))
                            .numberOfContractsInProgress(item.getInteger("inProgressContractsCount"))
                            .numberOfTerminatedContracts(item.getInteger("terminatedContractsCount"))
                            .notDoneDueDate(item.getDouble("outstandingAmount"))
                            .doneDueDate(item.getDouble("overdueAmount"));
                }
            }

            final JsonArray personRoles = iranianBase.getJsonArray("personRoles");
            if (personRoles != null && !personRoles.isEmpty()) {
                for (Object itemObj : personRoles) {
                    final JsonObject item = (JsonObject) itemObj;
                    dto.personaRoles()
                            .role(item.getString("title"))
                            .numberOfContract(item.getInteger("numberOfContracts"));
                }
            }
        }

        final JsonObject iranianStandard = getJsonByType(inquiryDetails , "IRANIAN_STANDARD");

        if (iranianStandard != null && !iranianStandard.isEmpty()) {
            final JsonArray personNegativeStatus = iranianStandard.getJsonArray("personNegativeStatus");
            if (personNegativeStatus != null && !personNegativeStatus.isEmpty()) {
                for (Object itemObj : personNegativeStatus) {
                    final JsonObject item = (JsonObject) itemObj;
                    dto.detailsPersonNegativeSituation()
                            .memberType(item.getString("organizationType"))
                            .memberName(item.getString("organizationName"))
                            .negativeStatusType(item.getString("state"))
                            .statusAnnouncementDate(item.getString("date"));
                }
            }

            final JsonArray directContracts = iranianStandard.getJsonArray("directContracts");
            if (directContracts != null && !directContracts.isEmpty()) {
                for (final Object itemObj : directContracts) {

                    final JsonObject item = (JsonObject) itemObj;

                    dto.facilities()
                            .contractNumber(item.getString("contractNumber"))
                            .level("درجریان");

                    dto.generalInformation()
                            .contractNumber(item.getString("contractNumber"))
                            .negativeStatusContract(item.getString("negativeContractStatus"))
                            .typeContractBank(item.getString("typeOfFinancingTitle"))
                            .startDate(item.getString("startDate"))
                            .estimatedCompletionDate(item.getString("realEndDate"))
                            .currency(item.getString("installmentsCurrency"))
                            .statusAnnouncementDate(item.getString("accountingDate"))
                            .purposeReceivingFacility(item.getString("purposeOfTheCredit"))
                            .rolePerson(item.getString("customerRoleTilte"))
                            .truster(item.getString("organizationType"));

                    /**
                     * @TODO -> relatedPersons
                     */
                    dto.otherRelatedParties().nationalNumber("4130626507").rolePerson("ضامن");

                    dto.contractDetails()
                            .contractNumber(item.getString("contractNumber"))
                            .amountDue(item.getDouble("outstandingAmount"))
                            .numberOfUnpaidDueInstallments(item.getInteger("totalNumberOfOverdue") == null ? 0 : item.getInteger("totalNumberOfOverdue")) // تعداد کل اقساد سررسید پرداخت نشده
                            .totalAmountContract(item.getDouble("totalAmount"))
                            .amountInstallment(item.getDouble("instalmentAmount"))
                            .totalNumberOfInstallments(item.getInteger("totalNumberOfInstallments"))
                            .amountNotDue(686_266_006L) // TODO
                            .numberOfDeferredInstallments(item.getInteger("totalNumberOfOutstanding") == null ? 0 :item.getInteger("totalNumberOfOutstanding")) // تعداد اقساط سررسید شده
                            .installmentType(item.getString("typeofInstallments"))
                            .paymentTurn(item.getString("paymentPeriodicity")) // نوبت پرداخت TODO
                            .paymentType(item.getString("paymentMethod")); // نوع پرداخت TODO
                }
            }
        }

        // TODO
        dto.reimbursementCalendar().yearMonth("1402/2").numberOfUnpaidDueInstallments(0).amountTheDueDebtHasNotBeenPaid(0);
        dto.reimbursementCalendar().yearMonth("1402/2").numberOfUnpaidDueInstallments(0).amountTheDueDebtHasNotBeenPaid(0);
        dto.reimbursementCalendar().yearMonth("1402/2").numberOfUnpaidDueInstallments(0).amountTheDueDebtHasNotBeenPaid(0);
        dto.reimbursementCalendar().yearMonth("1402/2").numberOfUnpaidDueInstallments(0).amountTheDueDebtHasNotBeenPaid(0);


        dto.facilities2().contractNumber("81933997856608").level("خاتمه یافته عادی طبق قرارداد");

        dto.generalInformation2()
                .contractNumber("413731089316600")
                .negativeStatusContract("فاقد وضعیت منفی")
                .typeContractBank("قرض الحسنه")
                .startDate("1400/04/08")
                .estimatedCompletionDate("1410/04/08")
                .currency("ریال")
                .statusAnnouncementDate("1402/01/05")
                .purposeReceivingFacility("سایر")
                .rolePerson("متقاضی اصلی")
                .truster("بانک پارسیان");

        dto.otherRelatedParties2().nationalNumber("4130626507").rolePerson("ضامن");
        dto.otherRelatedParties2().nationalNumber("4130626507").rolePerson("ضامن");

        dto.contractDetails2()
                .contractNumber("413731089316600")
                .amountDue(0)
                .numberOfUnpaidDueInstallments(0)
                .totalAmountContract(700_000_000L)
                .amountInstallment(6_363_000L)
                .totalNumberOfInstallments(120)
                .amountNotDue(686_266_006L)
                .numberOfDeferredInstallments(100)
                .installmentType("اقساط ثابت")
                .paymentTurn("ماهانه")
                .paymentType("اختیار برداشت از حساب");

        dto.reimbursementCalendar2().yearMonth("1402/2").numberOfUnpaidDueInstallments(0).amountTheDueDebtHasNotBeenPaid(0);
        dto.reimbursementCalendar2().yearMonth("1402/2").numberOfUnpaidDueInstallments(0).amountTheDueDebtHasNotBeenPaid(0);

        return dto;
    }

    private static JsonObject getJsonByType(final JsonArray inquiryDetails , final String type) throws Exception {
        final JsonObject result = (JsonObject) inquiryDetails.stream().filter(item -> {
            final JsonObject joItem = (JsonObject) item;
            return joItem.getString("type").equals(type);
        }).findAny().orElse(null);

        if (result != null) return result.getJsonObject("result").getJsonObject("data");
        return null;
    }

    @SuppressWarnings("unused")
	private static CreditReportNaturalPersonDto getExampleDto() {

        final CreditReportNaturalPersonDto dto = CreditReportNaturalPersonDto.dto();

        dto.personal()
                .firstName("بردیا")
                .lastName("نامجو")
                .fatherName("داریوش")
                .dateOfBirth("1375/06/25")
                .sex("مرد")
                .nationalCode("121545644")
                .placeOfBirth("مرودشت")
                .maritalStatus("مجرد")
                .address("مرودشت ، خیابان مدرس ، خیابان پیروزی ، بعد از فرعی 13 درب 4 سمت راست")
                .landlinePhone("07143321770");


        dto.setIranianScoreScore(613).setIranianScoreRisk("B2").setIranianScoreDescription("ریسک کم");

        dto.reasonsForPoints().code("REC1").description("5 ماه یا کمتر از تاریخ شروع آخرین قرارداد گذشته است");
        dto.reasonsForPoints().code("HAD3").description("مبلغ بدهی سررسیدشده پرداخت نشده بیش از 10 میلیون ریال بیش از 5 ماه آخر می باشد");

        dto.inquiryStatistics().memberType("بانک مرکزی").lastMonthInquiryCount(1).lastYearInquiryCount(2).personNegativeStatusCount(10).contractNegativeStatusCount(11);
        dto.inquiryStatistics().memberType("بانک مرکزی").lastMonthInquiryCount(1).lastYearInquiryCount(2).personNegativeStatusCount(10).contractNegativeStatusCount(11);

        dto.contractInformation().contractType("تسهیلات").truster("بانک پارسیان").currency("ریال").numberOfContractsInProgress(2).numberOfTerminatedContracts(1).notDoneDueDate(781_726_006L).doneDueDate(0);
        dto.contractInformation().contractType("تسهیلات").truster("بانک پارسیان").currency("ریال").numberOfContractsInProgress(2).numberOfTerminatedContracts(1).notDoneDueDate(781_726_006L).doneDueDate(0);
        dto.contractInformation().contractType("تسهیلات").truster("بانک پارسیان").currency("ریال").numberOfContractsInProgress(2).numberOfTerminatedContracts(1).notDoneDueDate(781_726_006L).doneDueDate(0);
        dto.contractInformation().contractType("تسهیلات").truster("بانک پارسیان").currency("ریال").numberOfContractsInProgress(2).numberOfTerminatedContracts(1).notDoneDueDate(781_726_006L).doneDueDate(0);
        dto.contractInformation().contractType("تسهیلات").truster("بانک پارسیان").currency("ریال").numberOfContractsInProgress(2).numberOfTerminatedContracts(1).notDoneDueDate(781_726_006L).doneDueDate(0);

        dto.personaRoles().role("متقاضی اصلی").numberOfContract(3);
        dto.personaRoles().role("ضامن").numberOfContract(3);

        dto.detailsPersonNegativeSituation().memberType("بانک ها و موسسات اعتباری").memberName("بانک پارسیان").negativeStatusType("فاقد وضعیت منفی").statusAnnouncementDate("1402/01/05");

        dto.facilities().contractNumber("880941451895607").level("درجریان");
        dto.facilities().contractNumber("880941451895607").level("درجریان");
        dto.facilities().contractNumber("880941451895607").level("درجریان");

        dto.generalInformation()
                .contractNumber("413731089316600")
                .negativeStatusContract("فاقد وضعیت منفی")
                .typeContractBank("قرض الحسنه")
                .startDate("1400/04/08")
                .estimatedCompletionDate("1410/04/08")
                .currency("ریال")
                .statusAnnouncementDate("1402/01/05")
                .purposeReceivingFacility("سایر")
                .rolePerson("متقاضی اصلی")
                .truster("بانک پارسیان");
        dto.generalInformation()
                .contractNumber("413731089316600")
                .negativeStatusContract("فاقد وضعیت منفی")
                .typeContractBank("قرض الحسنه")
                .startDate("1400/04/08")
                .estimatedCompletionDate("1410/04/08")
                .currency("ریال")
                .statusAnnouncementDate("1402/01/05")
                .purposeReceivingFacility("سایر")
                .rolePerson("متقاضی اصلی")
                .truster("بانک پارسیان");

        dto.otherRelatedParties().nationalNumber("4130626507").rolePerson("ضامن");
        dto.otherRelatedParties().nationalNumber("4130626507").rolePerson("ضامن");
        dto.otherRelatedParties().nationalNumber("4130626507").rolePerson("ضامن");
        dto.otherRelatedParties().nationalNumber("4130626507").rolePerson("ضامن");
        dto.otherRelatedParties().nationalNumber("4130626507").rolePerson("ضامن");

        dto.contractDetails()
                .contractNumber("413731089316600")
                .amountDue(0)
                .numberOfUnpaidDueInstallments(0)
                .totalAmountContract(700_000_000L)
                .amountInstallment(6_363_000L)
                .totalNumberOfInstallments(120)
                .amountNotDue(686_266_006L)
                .numberOfDeferredInstallments(100)
                .installmentType("اقساط ثابت")
                .paymentTurn("ماهانه")
                .paymentType("اختیار برداشت از حساب");
        dto.contractDetails()
                .contractNumber("413731089316600")
                .amountDue(0)
                .numberOfUnpaidDueInstallments(0)
                .totalAmountContract(700_000_000L)
                .amountInstallment(6_363_000L)
                .totalNumberOfInstallments(120)
                .amountNotDue(686_266_006L)
                .numberOfDeferredInstallments(100)
                .installmentType("اقساط ثابت")
                .paymentTurn("ماهانه")
                .paymentType("اختیار برداشت از حساب");


        dto.reimbursementCalendar().yearMonth("1402/2").numberOfUnpaidDueInstallments(0).amountTheDueDebtHasNotBeenPaid(0);
        dto.reimbursementCalendar().yearMonth("1402/2").numberOfUnpaidDueInstallments(0).amountTheDueDebtHasNotBeenPaid(0);
        dto.reimbursementCalendar().yearMonth("1402/2").numberOfUnpaidDueInstallments(0).amountTheDueDebtHasNotBeenPaid(0);
        dto.reimbursementCalendar().yearMonth("1402/2").numberOfUnpaidDueInstallments(0).amountTheDueDebtHasNotBeenPaid(0);

        dto.facilities2().contractNumber("81933997856608").level("خاتمه یافته عادی طبق قرارداد");

        dto.generalInformation2()
                .contractNumber("413731089316600")
                .negativeStatusContract("فاقد وضعیت منفی")
                .typeContractBank("قرض الحسنه")
                .startDate("1400/04/08")
                .estimatedCompletionDate("1410/04/08")
                .currency("ریال")
                .statusAnnouncementDate("1402/01/05")
                .purposeReceivingFacility("سایر")
                .rolePerson("متقاضی اصلی")
                .truster("بانک پارسیان");

        dto.otherRelatedParties2().nationalNumber("4130626507").rolePerson("ضامن");
        dto.otherRelatedParties2().nationalNumber("4130626507").rolePerson("ضامن");

        dto.contractDetails2()
                .contractNumber("413731089316600")
                .amountDue(0)
                .numberOfUnpaidDueInstallments(0)
                .totalAmountContract(700_000_000L)
                .amountInstallment(6_363_000L)
                .totalNumberOfInstallments(120)
                .amountNotDue(686_266_006L)
                .numberOfDeferredInstallments(100)
                .installmentType("اقساط ثابت")
                .paymentTurn("ماهانه")
                .paymentType("اختیار برداشت از حساب");

        dto.reimbursementCalendar2().yearMonth("1402/2").numberOfUnpaidDueInstallments(0).amountTheDueDebtHasNotBeenPaid(0);
        dto.reimbursementCalendar2().yearMonth("1402/2").numberOfUnpaidDueInstallments(0).amountTheDueDebtHasNotBeenPaid(0);

        dto.setDatePreparationOfTheReport("1402/03/09 12:57:53");

        return dto;
    }

}
