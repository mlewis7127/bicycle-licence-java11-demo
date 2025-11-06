
package uk.gov.dvla.poc.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import uk.gov.dvla.poc.events.ContactDetailsChanged;
import uk.gov.dvla.poc.events.NameChanged;
import uk.gov.dvla.poc.events.PenaltyPointsAdded;
import uk.gov.dvla.poc.events.PenaltyPointsRemoved;
import uk.gov.dvla.poc.forms.BicycleLicenceUpdateForm;
import uk.gov.dvla.poc.model.BicycleLicence;
import uk.gov.dvla.poc.repository.BicycleLicenceDynamoDBRepository;

import javax.servlet.http.HttpServletRequest;

class DynamoDBControllerTest {

    @InjectMocks
    private DynamoDBController dynamoDBController;

    @Mock
    private BicycleLicenceDynamoDBRepository licenceDynamoDBRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testUpdateLicenceForm_LicenceNotFound() {
        // Arrange
        BicycleLicenceUpdateForm form = new BicycleLicenceUpdateForm();
        form.setEmail("test@example.com");

        when(licenceDynamoDBRepository.findByEmail(form.getEmail())).thenReturn(null);

        // Act
        String result = dynamoDBController.updateLicenceForm(request, form, model);

        // Assert
        assertEquals("error", result);
        verify(licenceDynamoDBRepository).findByEmail(form.getEmail());
        verify(licenceDynamoDBRepository, never()).save(any(BicycleLicence.class));
    }

    @Test
    void testUpdateLicenceForm_AddPenaltyPoints() {
        // Arrange
        BicycleLicenceUpdateForm form = new BicycleLicenceUpdateForm();
        form.setEmail("test@example.com");
        form.setPenaltyPoints(5);

        BicycleLicence licence = spy(new BicycleLicence());
        licence.setPenaltyPoints(3);

        when(licenceDynamoDBRepository.findByEmail(form.getEmail())).thenReturn(licence);
        when(licenceDynamoDBRepository.save(any(BicycleLicence.class))).thenReturn(licence);

        // Act
        String result = dynamoDBController.updateLicenceForm(request, form, model);

        // Assert
        assertEquals("success", result);
        assertEquals(5, licence.getPenaltyPoints());
        verify(licence).addEvent(any(PenaltyPointsAdded.class));
        verify(licenceDynamoDBRepository).save(any(BicycleLicence.class));
    }

    @Test
    void testUpdateLicenceForm_RemovePenaltyPoints() {
        // Arrange
        BicycleLicenceUpdateForm form = new BicycleLicenceUpdateForm();
        form.setEmail("test@example.com");
        form.setPenaltyPoints(2);

        BicycleLicence licence = spy(new BicycleLicence());
        licence.setPenaltyPoints(5);

        when(licenceDynamoDBRepository.findByEmail(form.getEmail())).thenReturn(licence);
        when(licenceDynamoDBRepository.save(any(BicycleLicence.class))).thenReturn(licence);

        // Act
        String result = dynamoDBController.updateLicenceForm(request, form, model);

        // Assert
        assertEquals("success", result);
        assertEquals(2, licence.getPenaltyPoints());
        verify(licence).addEvent(any(PenaltyPointsRemoved.class));
        verify(licenceDynamoDBRepository).save(any(BicycleLicence.class));
    }

    @Test
    void testUpdateLicenceForm_ChangeName() {
        // Arrange
        BicycleLicenceUpdateForm form = new BicycleLicenceUpdateForm();
        form.setEmail("test@example.com");
        form.setPenaltyPoints(5);
        form.setName("New Name");

        BicycleLicence licence = spy(new BicycleLicence());
        licence.setPenaltyPoints(5);
        licence.setName("Old Name");

        when(licenceDynamoDBRepository.findByEmail(form.getEmail())).thenReturn(licence);
        when(licenceDynamoDBRepository.save(any(BicycleLicence.class))).thenReturn(licence);

        // Act
        String result = dynamoDBController.updateLicenceForm(request, form, model);

        // Assert
        assertEquals("success", result);
        assertEquals("New Name", licence.getName());
        verify(licence).addEvent(any(NameChanged.class));
        verify(licenceDynamoDBRepository).save(any(BicycleLicence.class));
    }


    @Test
    void testUpdateLicenceForm_ChangeTelephone() {
        // Arrange
        BicycleLicenceUpdateForm form = new BicycleLicenceUpdateForm();
        form.setEmail("test@example.com");
        form.setPenaltyPoints(5);
        form.setName("Same Name");
        form.setTelephone("1234567890");

        BicycleLicence licence = spy(new BicycleLicence());
        licence.setPenaltyPoints(5);
        licence.setName("Same Name");
        licence.setTelephone("0987654321");

        when(licenceDynamoDBRepository.findByEmail(form.getEmail())).thenReturn(licence);
        when(licenceDynamoDBRepository.save(any(BicycleLicence.class))).thenReturn(licence);

        // Act
        String result = dynamoDBController.updateLicenceForm(request, form, model);

        // Assert
        assertEquals("success", result);
        assertEquals("1234567890", licence.getTelephone());
        verify(licence).addEvent(any(ContactDetailsChanged.class));
        verify(licenceDynamoDBRepository).save(any(BicycleLicence.class));
    }


    @Test
    void testUpdateLicenceForm_NoChanges() {
        // Arrange
        BicycleLicenceUpdateForm form = new BicycleLicenceUpdateForm();
        form.setEmail("test@example.com");
        form.setPenaltyPoints(5);
        form.setName("Same Name");
        form.setTelephone("1234567890");

        BicycleLicence licence = spy(new BicycleLicence());
        licence.setPenaltyPoints(5);
        licence.setName("Same Name");
        licence.setTelephone("1234567890");

        when(licenceDynamoDBRepository.findByEmail(form.getEmail())).thenReturn(licence);
        when(licenceDynamoDBRepository.save(any(BicycleLicence.class))).thenReturn(licence);

        // Act
        String result = dynamoDBController.updateLicenceForm(request, form, model);

        // Assert
        assertEquals("success", result);
        verify(licence, never()).addEvent(any());
        verify(licenceDynamoDBRepository).save(any(BicycleLicence.class));
    }


}
