package com.nashss.se.hms.activity;

import com.nashss.se.hms.activity.requests.DeletePatientRequest;
import com.nashss.se.hms.activity.results.DeletePatientResult;
import com.nashss.se.hms.converters.ModelConverter;
import com.nashss.se.hms.dynamodb.DiagnosisDAO;
import com.nashss.se.hms.dynamodb.MedicationDAO;
import com.nashss.se.hms.dynamodb.PatientDAO;
import com.nashss.se.hms.dynamodb.models.Diagnosis;
import com.nashss.se.hms.dynamodb.models.Medication;
import com.nashss.se.hms.dynamodb.models.Patient;
import com.nashss.se.hms.models.PatientModel;

import javax.inject.Inject;
import java.util.List;

public class DeletePatientActivity {
    private final PatientDAO patientDAO;
    private final DiagnosisDAO diagnosisDAO;
    private final MedicationDAO medicationDAO;

    @Inject
    public DeletePatientActivity(PatientDAO patientDAO, DiagnosisDAO diagnosisDAO, MedicationDAO medicationDAO){
        this.patientDAO = patientDAO;
        this.diagnosisDAO = diagnosisDAO;
        this.medicationDAO = medicationDAO;
    }
    public DeletePatientResult handleRequest(final DeletePatientRequest deletePatientRequest){
        Patient patient = new Patient();

       List<Diagnosis> diagnosisList = diagnosisDAO.getDiagnoses(deletePatientRequest.getPatientId());
       List<Medication> medicationList =medicationDAO.getMedications(deletePatientRequest.getPatientId());
       diagnosisDAO.deleteDiagnosisBatch(diagnosisList);
       medicationDAO.deleteMedicationBatch(medicationList);
       patient.setPatientId(deletePatientRequest.getPatientId());
       patientDAO.deletePatient(patient);


        PatientModel patientModel = new ModelConverter().toPatientModel(patient);
        return DeletePatientResult.builder()
                .withPatientModel(patientModel)
                .build();
    }
}
