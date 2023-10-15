package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MedicalServiceImplTest {

    @Mock
    PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
    SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

    @ParameterizedTest
    @CsvSource ({
            "user1, 130, 80, 120, 80, 1", //  вывод сообщения во время проверки давления
            "user1, 120, 70, 120, 80, 1", //  вывод сообщения во время проверки давления
            "user1, 120, 80, 120, 80, 0"  //  сообщения не выводятся во время проверки давления
    })
    void checkBloodPressure(String id, int high, int low, int highExpected , int lowExpected, int countCallSend) {
        PatientInfo patient = new PatientInfo(id,"Семен", "Михайлов",
                LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(high, low)));

        Mockito.when(patientInfoRepository.getById(id)).thenReturn(patient);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        medicalService.checkBloodPressure(id,new BloodPressure(highExpected,lowExpected));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        if (countCallSend>0){
            Mockito.verify(sendAlertService).send(argumentCaptor.capture());
            Mockito.verify(sendAlertService, Mockito.times(countCallSend))
                    .send(argumentCaptor.getValue());
        }else {
            Mockito.verify(sendAlertService, Mockito.times(0))
                    .send(Mockito.any(String.class));
        }
    }

    @ParameterizedTest
    @CsvSource ({
            "user1, 36.6, 36.6, 0", //  сообщения не выводятся во время проверки температуры
            "user1, 37.6, 36.6, 0", //  сообщения не выводятся во время проверки температуры
            "user1, 38.2, 36.6, 1", //  вывод сообщения во время проверки температуры
    })
    void checkTemperature(String id, String temperature, String NormalTemperature, int countCallSend) {
        PatientInfo patient = new PatientInfo(id,"Семен", "Михайлов",
                LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal(temperature), new BloodPressure(120, 80)));

        Mockito.when(patientInfoRepository.getById(id)).thenReturn(patient);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        medicalService.checkTemperature(id,new BigDecimal(NormalTemperature));
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        if (countCallSend>0){
            Mockito.verify(sendAlertService).send(argumentCaptor.capture());
            Mockito.verify(sendAlertService, Mockito.times(countCallSend))
                    .send(argumentCaptor.getValue());
        }else {
            Mockito.verify(sendAlertService, Mockito.times(0))
                    .send(Mockito.any(String.class));
        }
    }
}