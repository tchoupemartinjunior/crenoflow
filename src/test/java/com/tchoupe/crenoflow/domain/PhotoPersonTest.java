package com.tchoupe.crenoflow.domain;

import static com.tchoupe.crenoflow.domain.PersonTestSamples.*;
import static com.tchoupe.crenoflow.domain.PhotoPersonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tchoupe.crenoflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhotoPersonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhotoPerson.class);
        PhotoPerson photoPerson1 = getPhotoPersonSample1();
        PhotoPerson photoPerson2 = new PhotoPerson();
        assertThat(photoPerson1).isNotEqualTo(photoPerson2);

        photoPerson2.setId(photoPerson1.getId());
        assertThat(photoPerson1).isEqualTo(photoPerson2);

        photoPerson2 = getPhotoPersonSample2();
        assertThat(photoPerson1).isNotEqualTo(photoPerson2);
    }

    @Test
    void personTest() throws Exception {
        PhotoPerson photoPerson = getPhotoPersonRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        photoPerson.setPerson(personBack);
        assertThat(photoPerson.getPerson()).isEqualTo(personBack);

        photoPerson.person(null);
        assertThat(photoPerson.getPerson()).isNull();
    }
}
