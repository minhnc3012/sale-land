package com.bh.saleland.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bh.saleland.IntegrationTest;
import com.bh.saleland.domain.LandCoordinate;
import com.bh.saleland.repository.LandCoordinateRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LandCoordinateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LandCoordinateResourceIT {

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final String ENTITY_API_URL = "/api/land-coordinates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LandCoordinateRepository landCoordinateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLandCoordinateMockMvc;

    private LandCoordinate landCoordinate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LandCoordinate createEntity(EntityManager em) {
        LandCoordinate landCoordinate = new LandCoordinate().latitude(DEFAULT_LATITUDE).longitude(DEFAULT_LONGITUDE);
        return landCoordinate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LandCoordinate createUpdatedEntity(EntityManager em) {
        LandCoordinate landCoordinate = new LandCoordinate().latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);
        return landCoordinate;
    }

    @BeforeEach
    public void initTest() {
        landCoordinate = createEntity(em);
    }

    @Test
    @Transactional
    void createLandCoordinate() throws Exception {
        int databaseSizeBeforeCreate = landCoordinateRepository.findAll().size();
        // Create the LandCoordinate
        restLandCoordinateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landCoordinate))
            )
            .andExpect(status().isCreated());

        // Validate the LandCoordinate in the database
        List<LandCoordinate> landCoordinateList = landCoordinateRepository.findAll();
        assertThat(landCoordinateList).hasSize(databaseSizeBeforeCreate + 1);
        LandCoordinate testLandCoordinate = landCoordinateList.get(landCoordinateList.size() - 1);
        assertThat(testLandCoordinate.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testLandCoordinate.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void createLandCoordinateWithExistingId() throws Exception {
        // Create the LandCoordinate with an existing ID
        landCoordinate.setId(1L);

        int databaseSizeBeforeCreate = landCoordinateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLandCoordinateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landCoordinate))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandCoordinate in the database
        List<LandCoordinate> landCoordinateList = landCoordinateRepository.findAll();
        assertThat(landCoordinateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLandCoordinates() throws Exception {
        // Initialize the database
        landCoordinateRepository.saveAndFlush(landCoordinate);

        // Get all the landCoordinateList
        restLandCoordinateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(landCoordinate.getId().intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())));
    }

    @Test
    @Transactional
    void getLandCoordinate() throws Exception {
        // Initialize the database
        landCoordinateRepository.saveAndFlush(landCoordinate);

        // Get the landCoordinate
        restLandCoordinateMockMvc
            .perform(get(ENTITY_API_URL_ID, landCoordinate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(landCoordinate.getId().intValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingLandCoordinate() throws Exception {
        // Get the landCoordinate
        restLandCoordinateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLandCoordinate() throws Exception {
        // Initialize the database
        landCoordinateRepository.saveAndFlush(landCoordinate);

        int databaseSizeBeforeUpdate = landCoordinateRepository.findAll().size();

        // Update the landCoordinate
        LandCoordinate updatedLandCoordinate = landCoordinateRepository.findById(landCoordinate.getId()).get();
        // Disconnect from session so that the updates on updatedLandCoordinate are not directly saved in db
        em.detach(updatedLandCoordinate);
        updatedLandCoordinate.latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);

        restLandCoordinateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLandCoordinate.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLandCoordinate))
            )
            .andExpect(status().isOk());

        // Validate the LandCoordinate in the database
        List<LandCoordinate> landCoordinateList = landCoordinateRepository.findAll();
        assertThat(landCoordinateList).hasSize(databaseSizeBeforeUpdate);
        LandCoordinate testLandCoordinate = landCoordinateList.get(landCoordinateList.size() - 1);
        assertThat(testLandCoordinate.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testLandCoordinate.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void putNonExistingLandCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = landCoordinateRepository.findAll().size();
        landCoordinate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLandCoordinateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, landCoordinate.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landCoordinate))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandCoordinate in the database
        List<LandCoordinate> landCoordinateList = landCoordinateRepository.findAll();
        assertThat(landCoordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLandCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = landCoordinateRepository.findAll().size();
        landCoordinate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandCoordinateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landCoordinate))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandCoordinate in the database
        List<LandCoordinate> landCoordinateList = landCoordinateRepository.findAll();
        assertThat(landCoordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLandCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = landCoordinateRepository.findAll().size();
        landCoordinate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandCoordinateMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landCoordinate))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LandCoordinate in the database
        List<LandCoordinate> landCoordinateList = landCoordinateRepository.findAll();
        assertThat(landCoordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLandCoordinateWithPatch() throws Exception {
        // Initialize the database
        landCoordinateRepository.saveAndFlush(landCoordinate);

        int databaseSizeBeforeUpdate = landCoordinateRepository.findAll().size();

        // Update the landCoordinate using partial update
        LandCoordinate partialUpdatedLandCoordinate = new LandCoordinate();
        partialUpdatedLandCoordinate.setId(landCoordinate.getId());

        partialUpdatedLandCoordinate.latitude(UPDATED_LATITUDE);

        restLandCoordinateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLandCoordinate.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLandCoordinate))
            )
            .andExpect(status().isOk());

        // Validate the LandCoordinate in the database
        List<LandCoordinate> landCoordinateList = landCoordinateRepository.findAll();
        assertThat(landCoordinateList).hasSize(databaseSizeBeforeUpdate);
        LandCoordinate testLandCoordinate = landCoordinateList.get(landCoordinateList.size() - 1);
        assertThat(testLandCoordinate.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testLandCoordinate.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void fullUpdateLandCoordinateWithPatch() throws Exception {
        // Initialize the database
        landCoordinateRepository.saveAndFlush(landCoordinate);

        int databaseSizeBeforeUpdate = landCoordinateRepository.findAll().size();

        // Update the landCoordinate using partial update
        LandCoordinate partialUpdatedLandCoordinate = new LandCoordinate();
        partialUpdatedLandCoordinate.setId(landCoordinate.getId());

        partialUpdatedLandCoordinate.latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);

        restLandCoordinateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLandCoordinate.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLandCoordinate))
            )
            .andExpect(status().isOk());

        // Validate the LandCoordinate in the database
        List<LandCoordinate> landCoordinateList = landCoordinateRepository.findAll();
        assertThat(landCoordinateList).hasSize(databaseSizeBeforeUpdate);
        LandCoordinate testLandCoordinate = landCoordinateList.get(landCoordinateList.size() - 1);
        assertThat(testLandCoordinate.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testLandCoordinate.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void patchNonExistingLandCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = landCoordinateRepository.findAll().size();
        landCoordinate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLandCoordinateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, landCoordinate.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(landCoordinate))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandCoordinate in the database
        List<LandCoordinate> landCoordinateList = landCoordinateRepository.findAll();
        assertThat(landCoordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLandCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = landCoordinateRepository.findAll().size();
        landCoordinate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandCoordinateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(landCoordinate))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandCoordinate in the database
        List<LandCoordinate> landCoordinateList = landCoordinateRepository.findAll();
        assertThat(landCoordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLandCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = landCoordinateRepository.findAll().size();
        landCoordinate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandCoordinateMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(landCoordinate))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LandCoordinate in the database
        List<LandCoordinate> landCoordinateList = landCoordinateRepository.findAll();
        assertThat(landCoordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLandCoordinate() throws Exception {
        // Initialize the database
        landCoordinateRepository.saveAndFlush(landCoordinate);

        int databaseSizeBeforeDelete = landCoordinateRepository.findAll().size();

        // Delete the landCoordinate
        restLandCoordinateMockMvc
            .perform(delete(ENTITY_API_URL_ID, landCoordinate.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LandCoordinate> landCoordinateList = landCoordinateRepository.findAll();
        assertThat(landCoordinateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
