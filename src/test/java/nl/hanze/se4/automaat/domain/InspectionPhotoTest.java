package nl.hanze.se4.automaat.domain;

import static nl.hanze.se4.automaat.domain.InspectionPhotoTestSamples.*;
import static nl.hanze.se4.automaat.domain.InspectionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import nl.hanze.se4.automaat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InspectionPhotoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InspectionPhoto.class);
        InspectionPhoto inspectionPhoto1 = getInspectionPhotoSample1();
        InspectionPhoto inspectionPhoto2 = new InspectionPhoto();
        assertThat(inspectionPhoto1).isNotEqualTo(inspectionPhoto2);

        inspectionPhoto2.setId(inspectionPhoto1.getId());
        assertThat(inspectionPhoto1).isEqualTo(inspectionPhoto2);

        inspectionPhoto2 = getInspectionPhotoSample2();
        assertThat(inspectionPhoto1).isNotEqualTo(inspectionPhoto2);
    }

    @Test
    void inspectionTest() {
        InspectionPhoto inspectionPhoto = getInspectionPhotoRandomSampleGenerator();
        Inspection inspectionBack = getInspectionRandomSampleGenerator();

        inspectionPhoto.setInspection(inspectionBack);
        assertThat(inspectionPhoto.getInspection()).isEqualTo(inspectionBack);

        inspectionPhoto.inspection(null);
        assertThat(inspectionPhoto.getInspection()).isNull();
    }
}
