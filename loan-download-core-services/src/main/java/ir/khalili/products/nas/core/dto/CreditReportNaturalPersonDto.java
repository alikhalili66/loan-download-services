package ir.khalili.products.nas.core.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author @bardiademon
 */
public final class CreditReportNaturalPersonDto {

    private PersonalInformation personal;

    private String iranianScoreRisk;
    private int iranianScoreScore;
    private String iranianScoreDescription;

    private final List<ReasonsForPoint> reasonsForPoints;
    private final List<InquiryStatistics> inquiryStatistics;
    private final List<ContractInformation> contractInformations;
    private final List<PersonRole> personRoles;
    private final List<DetailsPersonNegativeSituation> detailsPersonNegativeSituations;
    private final List<Facilities> facilities;
    private final List<GeneralInformation> generalInformations;
    private final List<OtherRelatedParties> otherRelatedParties;
    private final List<ContractDetails> contractDetails;
    private final List<ReimbursementCalendar> reimbursementCalendars;
    private final List<Facilities> facilities2;
    private final List<GeneralInformation> generalInformations2;
    private final List<OtherRelatedParties> otherRelatedParties2;
    private final List<ContractDetails> contractDetails2;
    private final List<ReimbursementCalendar> reimbursementCalendars2;

    private String datePreparationOfTheReport;

    private CreditReportNaturalPersonDto() {
        reasonsForPoints = new ArrayList<>();
        inquiryStatistics = new ArrayList<>();
        contractInformations = new ArrayList<>();
        personRoles = new ArrayList<>();
        detailsPersonNegativeSituations = new ArrayList<>();
        facilities = new ArrayList<>();
        generalInformations = new ArrayList<>();
        otherRelatedParties = new ArrayList<>();
        contractDetails = new ArrayList<>();
        reimbursementCalendars = new ArrayList<>();
        facilities2 = new ArrayList<>();
        generalInformations2 = new ArrayList<>();
        otherRelatedParties2 = new ArrayList<>();
        contractDetails2 = new ArrayList<>();
        reimbursementCalendars2 = new ArrayList<>();
    }

    public static CreditReportNaturalPersonDto dto() {
        return new CreditReportNaturalPersonDto();
    }

    public PersonalInformation personal() {
        return (personal = new PersonalInformation());
    }

    public CreditReportNaturalPersonDto setIranianScoreRisk(String iranianScoreRisk) {
        this.iranianScoreRisk = iranianScoreRisk;
        return this;
    }

    public CreditReportNaturalPersonDto setIranianScoreScore(int iranianScoreScore) {
        this.iranianScoreScore = iranianScoreScore;
        return this;
    }

    public CreditReportNaturalPersonDto setIranianScoreDescription(String iranianScoreDescription) {
        this.iranianScoreDescription = iranianScoreDescription;
        return this;
    }

    public String getIranianScoreRisk() {
        return iranianScoreRisk;
    }

    public int getIranianScoreScore() {
        return iranianScoreScore;
    }

    public String getIranianScoreDescription() {
        return iranianScoreDescription;
    }

    public ReasonsForPoint reasonsForPoints() {
        final ReasonsForPoint reasonsForPoint = new ReasonsForPoint();
        reasonsForPoints.add(reasonsForPoint);
        return reasonsForPoint;
    }

    public InquiryStatistics inquiryStatistics() {
        final InquiryStatistics inquiryStatistics = new InquiryStatistics();
        this.inquiryStatistics.add(inquiryStatistics);
        return inquiryStatistics;
    }

    public ContractInformation contractInformation() {
        final ContractInformation contractInformation = new ContractInformation();
        this.contractInformations.add(contractInformation);
        return contractInformation;
    }

    public PersonRole personaRoles() {
        final PersonRole personRole = new PersonRole();
        this.personRoles.add(personRole);
        return personRole;
    }

    public DetailsPersonNegativeSituation detailsPersonNegativeSituation() {
        final DetailsPersonNegativeSituation detailsPersonNegativeSituation = new DetailsPersonNegativeSituation();
        this.detailsPersonNegativeSituations.add(detailsPersonNegativeSituation);
        return detailsPersonNegativeSituation;
    }

    public Facilities facilities() {
        final Facilities facilities = new Facilities();
        this.facilities.add(facilities);
        return facilities;
    }

    public GeneralInformation generalInformation() {
        final GeneralInformation generalInformation = new GeneralInformation();
        this.generalInformations.add(generalInformation);
        return generalInformation;
    }

    public OtherRelatedParties otherRelatedParties() {
        final OtherRelatedParties otherRelatedParties = new OtherRelatedParties();
        this.otherRelatedParties.add(otherRelatedParties);
        return otherRelatedParties;
    }

    public ContractDetails contractDetails() {
        final ContractDetails contractDetails = new ContractDetails();
        this.contractDetails.add(contractDetails);
        return contractDetails;
    }

    public ReimbursementCalendar reimbursementCalendar() {
        final ReimbursementCalendar reimbursementCalendar = new ReimbursementCalendar();
        this.reimbursementCalendars.add(reimbursementCalendar);
        return reimbursementCalendar;
    }

    public Facilities facilities2() {
        final Facilities facilities = new Facilities();
        this.facilities2.add(facilities);
        return facilities;
    }

    public OtherRelatedParties otherRelatedParties2() {
        final OtherRelatedParties otherRelatedParties = new OtherRelatedParties();
        this.otherRelatedParties2.add(otherRelatedParties);
        return otherRelatedParties;
    }

    public GeneralInformation generalInformation2() {
        final GeneralInformation generalInformation = new GeneralInformation();
        this.generalInformations2.add(generalInformation);
        return generalInformation;
    }

    public ContractDetails contractDetails2() {
        final ContractDetails contractDetails = new ContractDetails();
        this.contractDetails2.add(contractDetails);
        return contractDetails;
    }

    public ReimbursementCalendar reimbursementCalendar2() {
        final ReimbursementCalendar reimbursementCalendar = new ReimbursementCalendar();
        this.reimbursementCalendars2.add(reimbursementCalendar);
        return reimbursementCalendar;
    }

    public PersonalInformation getPersonal() {
        return personal;
    }

    public List<ReasonsForPoint> getReasonsForPoints() {
        return reasonsForPoints;
    }

    public List<InquiryStatistics> getInquiryStatistics() {
        return inquiryStatistics;
    }

    public List<ContractInformation> getContractInformations() {
        return contractInformations;
    }

    public List<PersonRole> getPersonRoles() {
        return personRoles;
    }

    public List<DetailsPersonNegativeSituation> getDetailsPersonNegativeSituations() {
        return detailsPersonNegativeSituations;
    }

    public List<Facilities> getFacilities() {
        return facilities;
    }

    public List<GeneralInformation> getGeneralInformations() {
        return generalInformations;
    }

    public List<OtherRelatedParties> getOtherRelatedParties() {
        return otherRelatedParties;
    }

    public List<ContractDetails> getContractDetails() {
        return contractDetails;
    }

    public List<ReimbursementCalendar> getReimbursementCalendars() {
        return reimbursementCalendars;
    }

    public List<Facilities> getFacilities2() {
        return facilities2;
    }

    public List<GeneralInformation> getGeneralInformations2() {
        return generalInformations2;
    }


    public List<OtherRelatedParties> getOtherRelatedParties2() {
        return otherRelatedParties2;
    }

    public List<ContractDetails> getContractDetails2() {
        return contractDetails2;
    }

    public List<ReimbursementCalendar> getReimbursementCalendars2() {
        return reimbursementCalendars2;
    }

    public CreditReportNaturalPersonDto setDatePreparationOfTheReport(String datePreparationOfTheReport) {
        this.datePreparationOfTheReport = datePreparationOfTheReport;
        return this;
    }

    public String getDatePreparationOfTheReport() {
        return datePreparationOfTheReport;
    }

    public static final class PersonalInformation {
        private String firstName;
        private String lastName;
        private String nationalCode;
        private String fatherName;
        private String placeOfBirth;
        private String dateOfBirth;
        private String sex;
        private String maritalStatus;

        private String address;

        private String landlinePhone;

        private PersonalInformation() {
        }

        public PersonalInformation firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public PersonalInformation lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public PersonalInformation nationalCode(final String nationalCode) {
            this.nationalCode = nationalCode;
            return this;
        }

        public PersonalInformation fatherName(final String fatherName) {
            this.fatherName = fatherName;
            return this;
        }

        public PersonalInformation placeOfBirth(final String placeOfBirth) {
            this.placeOfBirth = placeOfBirth;
            return this;
        }

        public PersonalInformation dateOfBirth(final String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public PersonalInformation sex(final String sex) {
            this.sex = sex;
            return this;
        }

        public PersonalInformation maritalStatus(final String maritalStatus) {
            this.maritalStatus = maritalStatus;
            return this;
        }

        public PersonalInformation address(final String address) {
            this.address = address;
            return this;
        }

        public PersonalInformation landlinePhone(final String landlinePhone) {
            this.landlinePhone = landlinePhone;
            return this;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getNationalCode() {
            return nationalCode;
        }

        public String getFatherName() {
            return fatherName;
        }

        public String getPlaceOfBirth() {
            return placeOfBirth;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public String getSex() {
            return sex;
        }

        public String getMaritalStatus() {
            return maritalStatus;
        }

        public String getAddress() {
            return address;
        }

        public String getLandlinePhone() {
            return landlinePhone;
        }
    }

    public static final class ReasonsForPoint {
        private String code;
        private String description;

        private ReasonsForPoint() {
        }

        public ReasonsForPoint code(final String code) {
            this.code = code;
            return this;
        }

        public ReasonsForPoint description(final String description) {
            this.description = description;
            return this;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }


    }

    public static final class InquiryStatistics {
        private String memberType;
        private int lastMonthInquiryCount;
        private int lastYearInquiryCount;
        private int personNegativeStatusCount;
        private int contractNegativeStatusCount;

        public InquiryStatistics memberType(final String memberType) {
            this.memberType = memberType;
            return this;
        }

        public InquiryStatistics lastMonthInquiryCount(final int lastMonthInquiryCount) {
            this.lastMonthInquiryCount = lastMonthInquiryCount;
            return this;
        }

        public InquiryStatistics lastYearInquiryCount(final int lastYearInquiryCount) {
            this.lastYearInquiryCount = lastYearInquiryCount;
            return this;
        }

        public InquiryStatistics personNegativeStatusCount(final int personNegativeStatusCount) {
            this.personNegativeStatusCount = personNegativeStatusCount;
            return this;
        }

        public InquiryStatistics contractNegativeStatusCount(final int contractNegativeStatusCount) {
            this.contractNegativeStatusCount = contractNegativeStatusCount;
            return this;
        }

        public String getMemberType() {
            return memberType;
        }

        public int getLastMonthInquiryCount() {
            return lastMonthInquiryCount;
        }

        public int getLastYearInquiryCount() {
            return lastYearInquiryCount;
        }

        public int getPersonNegativeStatusCount() {
            return personNegativeStatusCount;
        }

        public int getContractNegativeStatusCount() {
            return contractNegativeStatusCount;
        }
    }

    public static final class ContractInformation {
        private String contractType;
        private String truster;
        private String currency;
        private int numberOfContractsInProgress;
        private int numberOfTerminatedContracts;
        private double notDoneDueDate;
        private double doneDueDate;

        public ContractInformation contractType(final String contractType) {
            this.contractType = contractType;
            return this;
        }

        public ContractInformation truster(final String truster) {
            this.truster = truster;
            return this;
        }

        public ContractInformation currency(final String currency) {
            this.currency = currency;
            return this;
        }

        public ContractInformation numberOfContractsInProgress(final int numberOfContractsInProgress) {
            this.numberOfContractsInProgress = numberOfContractsInProgress;
            return this;
        }

        public ContractInformation numberOfTerminatedContracts(final int numberOfTerminatedContracts) {
            this.numberOfTerminatedContracts = numberOfTerminatedContracts;
            return this;
        }

        public ContractInformation notDoneDueDate(final double notDoneDueDate) {
            this.notDoneDueDate = notDoneDueDate;
            return this;
        }

        public ContractInformation doneDueDate(final double doneDueDate) {
            this.doneDueDate = doneDueDate;
            return this;
        }

        public String getContractType() {
            return contractType;
        }

        public String getTruster() {
            return truster;
        }

        public String getCurrency() {
            return currency;
        }

        public int getNumberOfContractsInProgress() {
            return numberOfContractsInProgress;
        }

        public int getNumberOfTerminatedContracts() {
            return numberOfTerminatedContracts;
        }

        public double getNotDoneDueDate() {
            return notDoneDueDate;
        }

        public double getDoneDueDate() {
            return doneDueDate;
        }
    }

    public static final class PersonRole {

        private String role;
        private int numberOfContract;

        public PersonRole role(final String role) {
            this.role = role;
            return this;
        }

        public PersonRole numberOfContract(final int numberOfContract) {
            this.numberOfContract = numberOfContract;
            return this;
        }

        public String getRole() {
            return role;
        }

        public int getNumberOfContract() {
            return numberOfContract;
        }
    }

    public static final class DetailsPersonNegativeSituation {
        private String memberType;
        private String memberName;
        private String negativeStatusType;
        private String statusAnnouncementDate;

        public DetailsPersonNegativeSituation memberType(final String memberType) {
            this.memberType = memberType;
            return this;
        }

        public DetailsPersonNegativeSituation memberName(final String memberName) {
            this.memberName = memberName;
            return this;
        }

        public DetailsPersonNegativeSituation negativeStatusType(final String negativeStatusType) {
            this.negativeStatusType = negativeStatusType;
            return this;
        }

        public DetailsPersonNegativeSituation statusAnnouncementDate(final String statusAnnouncementDate) {
            this.statusAnnouncementDate = statusAnnouncementDate;
            return this;
        }

        public String getMemberType() {
            return memberType;
        }

        public String getMemberName() {
            return memberName;
        }

        public String getNegativeStatusType() {
            return negativeStatusType;
        }

        public String getStatusAnnouncementDate() {
            return statusAnnouncementDate;
        }
    }

    public static final class Facilities {
        private String contractNumber;
        private String level;

        public Facilities contractNumber(final String contractNumber) {
            this.contractNumber = contractNumber;
            return this;
        }

        public Facilities level(final String level) {
            this.level = level;
            return this;
        }

        public String getContractNumber() {
            return contractNumber;
        }

        public String getLevel() {
            return level;
        }
    }

    public static final class GeneralInformation {
        private String contractNumber;
        private String negativeStatusContract;
        private String typeContractBank;
        private String startDate;
        private String estimatedCompletionDate;
        private String currency;
        private String statusAnnouncementDate;
        private String purposeReceivingFacility;
        private String rolePerson;
        private String truster;

        public GeneralInformation contractNumber(final String contractNumber) {
            this.contractNumber = contractNumber;
            return this;
        }

        public GeneralInformation negativeStatusContract(final String negativeStatusContract) {
            this.negativeStatusContract = negativeStatusContract;
            return this;
        }

        public GeneralInformation typeContractBank(final String typeContractBank) {
            this.typeContractBank = typeContractBank;
            return this;
        }

        public GeneralInformation startDate(final String startDate) {
            this.startDate = startDate;
            return this;
        }

        public GeneralInformation estimatedCompletionDate(final String estimatedCompletionDate) {
            this.estimatedCompletionDate = estimatedCompletionDate;
            return this;
        }

        public GeneralInformation currency(final String currency) {
            this.currency = currency;
            return this;
        }

        public GeneralInformation statusAnnouncementDate(final String statusAnnouncementDate) {
            this.statusAnnouncementDate = statusAnnouncementDate;
            return this;
        }

        public GeneralInformation purposeReceivingFacility(final String purposeReceivingFacility) {
            this.purposeReceivingFacility = purposeReceivingFacility;
            return this;
        }

        public GeneralInformation rolePerson(final String rolePerson) {
            this.rolePerson = rolePerson;
            return this;
        }

        public GeneralInformation truster(final String truster) {
            this.truster = truster;
            return this;
        }

        public String getContractNumber() {
            return contractNumber;
        }

        public String getNegativeStatusContract() {
            return negativeStatusContract;
        }

        public String getTypeContractBank() {
            return typeContractBank;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEstimatedCompletionDate() {
            return estimatedCompletionDate;
        }

        public String getCurrency() {
            return currency;
        }

        public String getStatusAnnouncementDate() {
            return statusAnnouncementDate;
        }

        public String getPurposeReceivingFacility() {
            return purposeReceivingFacility;
        }

        public String getRolePerson() {
            return rolePerson;
        }

        public String getTruster() {
            return truster;
        }
    }

    public static final class OtherRelatedParties {
        private String nationalNumber;
        private String rolePerson;

        public OtherRelatedParties nationalNumber(final String nationalNumber) {
            this.nationalNumber = nationalNumber;
            return this;
        }

        public OtherRelatedParties rolePerson(final String rolePerson) {
            this.rolePerson = rolePerson;
            return this;
        }

        public String getNationalNumber() {
            return nationalNumber;
        }

        public String getRolePerson() {
            return rolePerson;
        }
    }

    public static final class ContractDetails {
        private String contractNumber;
        private double amountDue;
        private int numberOfUnpaidDueInstallments;
        private double totalAmountContract;
        private double amountInstallment;
        private int TotalNumberOfInstallments;
        private long amountNotDue;
        private int numberOfDeferredInstallments;
        private String installmentType;
        private String paymentTurn;
        private String paymentType;

        public ContractDetails contractNumber(final String contractNumber) {
            this.contractNumber = contractNumber;
            return this;
        }

        public ContractDetails amountDue(final double amountDue) {
            this.amountDue = amountDue;
            return this;
        }

        public ContractDetails numberOfUnpaidDueInstallments(final int numberOfUnpaidDueInstallments) {
            this.numberOfUnpaidDueInstallments = numberOfUnpaidDueInstallments;
            return this;
        }

        public ContractDetails totalAmountContract(final double totalAmountContract) {
            this.totalAmountContract = totalAmountContract;
            return this;
        }

        public ContractDetails amountInstallment(final double amountInstallment) {
            this.amountInstallment = amountInstallment;
            return this;
        }

        public ContractDetails totalNumberOfInstallments(final int totalNumberOfInstallments) {
            TotalNumberOfInstallments = totalNumberOfInstallments;
            return this;
        }

        public ContractDetails amountNotDue(final long amountNotDue) {
            this.amountNotDue = amountNotDue;
            return this;
        }

        public ContractDetails numberOfDeferredInstallments(final int numberOfDeferredInstallments) {
            this.numberOfDeferredInstallments = numberOfDeferredInstallments;
            return this;
        }

        public ContractDetails installmentType(final String installmentType) {
            this.installmentType = installmentType;
            return this;
        }

        public ContractDetails paymentTurn(final String paymentTurn) {
            this.paymentTurn = paymentTurn;
            return this;
        }

        public ContractDetails paymentType(final String paymentType) {
            this.paymentType = paymentType;
            return this;
        }

        public String getContractNumber() {
            return contractNumber;
        }

        public double getAmountDue() {
            return amountDue;
        }

        public int getNumberOfUnpaidDueInstallments() {
            return numberOfUnpaidDueInstallments;
        }

        public double getTotalAmountContract() {
            return totalAmountContract;
        }

        public double getAmountInstallment() {
            return amountInstallment;
        }

        public int getTotalNumberOfInstallments() {
            return TotalNumberOfInstallments;
        }

        public long getAmountNotDue() {
            return amountNotDue;
        }

        public long getNumberOfDeferredInstallments() {
            return numberOfDeferredInstallments;
        }

        public String getInstallmentType() {
            return installmentType;
        }

        public String getPaymentTurn() {
            return paymentTurn;
        }

        public String getPaymentType() {
            return paymentType;
        }
    }

    public static final class ReimbursementCalendar {
        private String yearMonth;
        private int numberOfUnpaidDueInstallments;
        private long amountTheDueDebtHasNotBeenPaid;

        public ReimbursementCalendar yearMonth(final String yearMonth) {
            this.yearMonth = yearMonth;
            return this;
        }

        public ReimbursementCalendar numberOfUnpaidDueInstallments(final int numberOfUnpaidDueInstallments) {
            this.numberOfUnpaidDueInstallments = numberOfUnpaidDueInstallments;
            return this;
        }

        public ReimbursementCalendar amountTheDueDebtHasNotBeenPaid(final long amountTheDueDebtHasNotBeenPaid) {
            this.amountTheDueDebtHasNotBeenPaid = amountTheDueDebtHasNotBeenPaid;
            return this;
        }

        public String getYearMonth() {
            return yearMonth;
        }

        public int getNumberOfUnpaidDueInstallments() {
            return numberOfUnpaidDueInstallments;
        }

        public long getAmountTheDueDebtHasNotBeenPaid() {
            return amountTheDueDebtHasNotBeenPaid;
        }
    }
}
