
package com.capgemini.transfer.ws.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.capgemini.transfer.ws.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CorrelationHeader_QNAME = new QName("http://ATS.online-onboarding.com/Client/HRDataServiceEx", "CorrelationHeader");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.capgemini.transfer.ws.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BeginSession }
     * 
     */
    public BeginSession createBeginSession() {
        return new BeginSession();
    }

    /**
     * Create an instance of {@link BeginSessionResponse }
     * 
     */
    public BeginSessionResponse createBeginSessionResponse() {
        return new BeginSessionResponse();
    }

    /**
     * Create an instance of {@link CorrelationHeader }
     * 
     */
    public CorrelationHeader createCorrelationHeader() {
        return new CorrelationHeader();
    }

    /**
     * Create an instance of {@link CancelSession }
     * 
     */
    public CancelSession createCancelSession() {
        return new CancelSession();
    }

    /**
     * Create an instance of {@link CancelSessionResponse }
     * 
     */
    public CancelSessionResponse createCancelSessionResponse() {
        return new CancelSessionResponse();
    }

    /**
     * Create an instance of {@link PostNewhireRecord }
     * 
     */
    public PostNewhireRecord createPostNewhireRecord() {
        return new PostNewhireRecord();
    }

    /**
     * Create an instance of {@link PostNewhireRecordResponse }
     * 
     */
    public PostNewhireRecordResponse createPostNewhireRecordResponse() {
        return new PostNewhireRecordResponse();
    }

    /**
     * Create an instance of {@link PostNewhireRecords }
     * 
     */
    public PostNewhireRecords createPostNewhireRecords() {
        return new PostNewhireRecords();
    }

    /**
     * Create an instance of {@link PostNewhireRecordsResponse }
     * 
     */
    public PostNewhireRecordsResponse createPostNewhireRecordsResponse() {
        return new PostNewhireRecordsResponse();
    }

    /**
     * Create an instance of {@link PostOffboardUserRecords }
     * 
     */
    public PostOffboardUserRecords createPostOffboardUserRecords() {
        return new PostOffboardUserRecords();
    }

    /**
     * Create an instance of {@link PostOffboardUserRecordsResponse }
     * 
     */
    public PostOffboardUserRecordsResponse createPostOffboardUserRecordsResponse() {
        return new PostOffboardUserRecordsResponse();
    }

    /**
     * Create an instance of {@link PostNewhireRecordWithSupplementalData }
     * 
     */
    public PostNewhireRecordWithSupplementalData createPostNewhireRecordWithSupplementalData() {
        return new PostNewhireRecordWithSupplementalData();
    }

    /**
     * Create an instance of {@link PostNewhireRecordWithSupplementalDataResponse }
     * 
     */
    public PostNewhireRecordWithSupplementalDataResponse createPostNewhireRecordWithSupplementalDataResponse() {
        return new PostNewhireRecordWithSupplementalDataResponse();
    }

    /**
     * Create an instance of {@link PostBase64NewhireRecord }
     * 
     */
    public PostBase64NewhireRecord createPostBase64NewhireRecord() {
        return new PostBase64NewhireRecord();
    }

    /**
     * Create an instance of {@link PostBase64NewhireRecordResponse }
     * 
     */
    public PostBase64NewhireRecordResponse createPostBase64NewhireRecordResponse() {
        return new PostBase64NewhireRecordResponse();
    }

    /**
     * Create an instance of {@link GetNewhireRecord }
     * 
     */
    public GetNewhireRecord createGetNewhireRecord() {
        return new GetNewhireRecord();
    }

    /**
     * Create an instance of {@link GetNewhireRecordResponse }
     * 
     */
    public GetNewhireRecordResponse createGetNewhireRecordResponse() {
        return new GetNewhireRecordResponse();
    }

    /**
     * Create an instance of {@link GetNewhireRecordByExportId }
     * 
     */
    public GetNewhireRecordByExportId createGetNewhireRecordByExportId() {
        return new GetNewhireRecordByExportId();
    }

    /**
     * Create an instance of {@link GetNewhireRecordByExportIdResponse }
     * 
     */
    public GetNewhireRecordByExportIdResponse createGetNewhireRecordByExportIdResponse() {
        return new GetNewhireRecordByExportIdResponse();
    }

    /**
     * Create an instance of {@link DeleteNewhireRecord }
     * 
     */
    public DeleteNewhireRecord createDeleteNewhireRecord() {
        return new DeleteNewhireRecord();
    }

    /**
     * Create an instance of {@link DeleteNewhireRecordResponse }
     * 
     */
    public DeleteNewhireRecordResponse createDeleteNewhireRecordResponse() {
        return new DeleteNewhireRecordResponse();
    }

    /**
     * Create an instance of {@link PostReferenceFiles }
     * 
     */
    public PostReferenceFiles createPostReferenceFiles() {
        return new PostReferenceFiles();
    }

    /**
     * Create an instance of {@link PostReferenceFilesResponse }
     * 
     */
    public PostReferenceFilesResponse createPostReferenceFilesResponse() {
        return new PostReferenceFilesResponse();
    }

    /**
     * Create an instance of {@link SyncUser }
     * 
     */
    public SyncUser createSyncUser() {
        return new SyncUser();
    }

    /**
     * Create an instance of {@link SyncUserResponse }
     * 
     */
    public SyncUserResponse createSyncUserResponse() {
        return new SyncUserResponse();
    }

    /**
     * Create an instance of {@link SyncUserWithEmail }
     * 
     */
    public SyncUserWithEmail createSyncUserWithEmail() {
        return new SyncUserWithEmail();
    }

    /**
     * Create an instance of {@link SyncUserWithEmailResponse }
     * 
     */
    public SyncUserWithEmailResponse createSyncUserWithEmailResponse() {
        return new SyncUserWithEmailResponse();
    }

    /**
     * Create an instance of {@link SyncUserWithCorporateCodes }
     * 
     */
    public SyncUserWithCorporateCodes createSyncUserWithCorporateCodes() {
        return new SyncUserWithCorporateCodes();
    }

    /**
     * Create an instance of {@link SyncUserWithCorporateCodesResponse }
     * 
     */
    public SyncUserWithCorporateCodesResponse createSyncUserWithCorporateCodesResponse() {
        return new SyncUserWithCorporateCodesResponse();
    }

    /**
     * Create an instance of {@link SyncUsers }
     * 
     */
    public SyncUsers createSyncUsers() {
        return new SyncUsers();
    }

    /**
     * Create an instance of {@link SyncUsersResponse }
     * 
     */
    public SyncUsersResponse createSyncUsersResponse() {
        return new SyncUsersResponse();
    }

    /**
     * Create an instance of {@link GetStatus }
     * 
     */
    public GetStatus createGetStatus() {
        return new GetStatus();
    }

    /**
     * Create an instance of {@link GetStatusResponse }
     * 
     */
    public GetStatusResponse createGetStatusResponse() {
        return new GetStatusResponse();
    }

    /**
     * Create an instance of {@link GetActivityStatus }
     * 
     */
    public GetActivityStatus createGetActivityStatus() {
        return new GetActivityStatus();
    }

    /**
     * Create an instance of {@link GetActivityStatusResponse }
     * 
     */
    public GetActivityStatusResponse createGetActivityStatusResponse() {
        return new GetActivityStatusResponse();
    }

    /**
     * Create an instance of {@link GetActivityStatusEx }
     * 
     */
    public GetActivityStatusEx createGetActivityStatusEx() {
        return new GetActivityStatusEx();
    }

    /**
     * Create an instance of {@link GetActivityStatusExResponse }
     * 
     */
    public GetActivityStatusExResponse createGetActivityStatusExResponse() {
        return new GetActivityStatusExResponse();
    }

    /**
     * Create an instance of {@link DeleteUser }
     * 
     */
    public DeleteUser createDeleteUser() {
        return new DeleteUser();
    }

    /**
     * Create an instance of {@link DeleteUserResponse }
     * 
     */
    public DeleteUserResponse createDeleteUserResponse() {
        return new DeleteUserResponse();
    }

    /**
     * Create an instance of {@link AddHRDataRecord }
     * 
     */
    public AddHRDataRecord createAddHRDataRecord() {
        return new AddHRDataRecord();
    }

    /**
     * Create an instance of {@link AddHRDataRecordResponse }
     * 
     */
    public AddHRDataRecordResponse createAddHRDataRecordResponse() {
        return new AddHRDataRecordResponse();
    }

    /**
     * Create an instance of {@link DeleteReferenceFiles }
     * 
     */
    public DeleteReferenceFiles createDeleteReferenceFiles() {
        return new DeleteReferenceFiles();
    }

    /**
     * Create an instance of {@link DeleteReferenceFilesResponse }
     * 
     */
    public DeleteReferenceFilesResponse createDeleteReferenceFilesResponse() {
        return new DeleteReferenceFilesResponse();
    }

    /**
     * Create an instance of {@link GetUserActivities }
     * 
     */
    public GetUserActivities createGetUserActivities() {
        return new GetUserActivities();
    }

    /**
     * Create an instance of {@link GetUserActivitiesResponse }
     * 
     */
    public GetUserActivitiesResponse createGetUserActivitiesResponse() {
        return new GetUserActivitiesResponse();
    }

    /**
     * Create an instance of {@link GetOffboardingActivityStatus }
     * 
     */
    public GetOffboardingActivityStatus createGetOffboardingActivityStatus() {
        return new GetOffboardingActivityStatus();
    }

    /**
     * Create an instance of {@link GetOffboardingActivityStatusResponse }
     * 
     */
    public GetOffboardingActivityStatusResponse createGetOffboardingActivityStatusResponse() {
        return new GetOffboardingActivityStatusResponse();
    }

    /**
     * Create an instance of {@link DeleteOffboardRecord }
     * 
     */
    public DeleteOffboardRecord createDeleteOffboardRecord() {
        return new DeleteOffboardRecord();
    }

    /**
     * Create an instance of {@link DeleteOffboardRecordResponse }
     * 
     */
    public DeleteOffboardRecordResponse createDeleteOffboardRecordResponse() {
        return new DeleteOffboardRecordResponse();
    }

    /**
     * Create an instance of {@link GetOffboardUserRecord }
     * 
     */
    public GetOffboardUserRecord createGetOffboardUserRecord() {
        return new GetOffboardUserRecord();
    }

    /**
     * Create an instance of {@link GetOffboardUserRecordResponse }
     * 
     */
    public GetOffboardUserRecordResponse createGetOffboardUserRecordResponse() {
        return new GetOffboardUserRecordResponse();
    }

    /**
     * Create an instance of {@link GetOffboardUserRecordWithExportId }
     * 
     */
    public GetOffboardUserRecordWithExportId createGetOffboardUserRecordWithExportId() {
        return new GetOffboardUserRecordWithExportId();
    }

    /**
     * Create an instance of {@link GetOffboardUserRecordWithExportIdResponse }
     * 
     */
    public GetOffboardUserRecordWithExportIdResponse createGetOffboardUserRecordWithExportIdResponse() {
        return new GetOffboardUserRecordWithExportIdResponse();
    }

    /**
     * Create an instance of {@link DeleteHRDataRecord }
     * 
     */
    public DeleteHRDataRecord createDeleteHRDataRecord() {
        return new DeleteHRDataRecord();
    }

    /**
     * Create an instance of {@link DeleteHRDataRecordResponse }
     * 
     */
    public DeleteHRDataRecordResponse createDeleteHRDataRecordResponse() {
        return new DeleteHRDataRecordResponse();
    }

    /**
     * Create an instance of {@link PostOffboardUserRecord }
     * 
     */
    public PostOffboardUserRecord createPostOffboardUserRecord() {
        return new PostOffboardUserRecord();
    }

    /**
     * Create an instance of {@link PostOffboardUserRecordResponse }
     * 
     */
    public PostOffboardUserRecordResponse createPostOffboardUserRecordResponse() {
        return new PostOffboardUserRecordResponse();
    }

    /**
     * Create an instance of {@link OffboardExistingUser }
     * 
     */
    public OffboardExistingUser createOffboardExistingUser() {
        return new OffboardExistingUser();
    }

    /**
     * Create an instance of {@link OffboardExistingUserResponse }
     * 
     */
    public OffboardExistingUserResponse createOffboardExistingUserResponse() {
        return new OffboardExistingUserResponse();
    }

    /**
     * Create an instance of {@link PostSebEvent }
     * 
     */
    public PostSebEvent createPostSebEvent() {
        return new PostSebEvent();
    }

    /**
     * Create an instance of {@link PostSebEventResponse }
     * 
     */
    public PostSebEventResponse createPostSebEventResponse() {
        return new PostSebEventResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CorrelationHeader }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ATS.online-onboarding.com/Client/HRDataServiceEx", name = "CorrelationHeader")
    public JAXBElement<CorrelationHeader> createCorrelationHeader(CorrelationHeader value) {
        return new JAXBElement<CorrelationHeader>(_CorrelationHeader_QNAME, CorrelationHeader.class, null, value);
    }

}
