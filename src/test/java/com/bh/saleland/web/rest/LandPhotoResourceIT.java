package com.bh.saleland.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bh.saleland.IntegrationTest;
import com.bh.saleland.domain.LandPhoto;
import com.bh.saleland.repository.LandPhotoRepository;
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
 * Integration tests for the {@link LandPhotoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LandPhotoResourceIT {

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/land-photos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LandPhotoRepository landPhotoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLandPhotoMockMvc;

    private LandPhoto landPhoto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LandPhoto createEntity(EntityManager em) {
        LandPhoto landPhoto = new LandPhoto().imageUrl(DEFAULT_IMAGE_URL);
        return landPhoto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LandPhoto createUpdatedEntity(EntityManager em) {
        LandPhoto landPhoto = new LandPhoto().imageUrl(UPDATED_IMAGE_URL);
        return landPhoto;
    }

    @BeforeEach
    public void initTest() {
        landPhoto = createEntity(em);
    }

    @Test
    @Transactional
    void createLandPhoto() throws Exception {
        int databaseSizeBeforeCreate = landPhotoRepository.findAll().size();
        // Create the LandPhoto
        restLandPhotoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landPhoto))
            )
            .andExpect(status().isCreated());

        // Validate the LandPhoto in the database
        List<LandPhoto> landPhotoList = landPhotoRepository.findAll();
        assertThat(landPhotoList).hasSize(databaseSizeBeforeCreate + 1);
        LandPhoto testLandPhoto = landPhotoList.get(landPhotoList.size() - 1);
        assertThat(testLandPhoto.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
    }

    @Test
    @Transactional
    void createLandPhotoWithExistingId() throws Exception {
        // Create the LandPhoto with an existing ID
        landPhoto.setId(1L);

        int databaseSizeBeforeCreate = landPhotoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLandPhotoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landPhoto))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandPhoto in the database
        List<LandPhoto> landPhotoList = landPhotoRepository.findAll();
        assertThat(landPhotoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLandPhotos() throws Exception {
        // Initialize the database
        landPhotoRepository.saveAndFlush(landPhoto);

        // Get all the landPhotoList
        restLandPhotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(landPhoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)));
    }

    @Test
    @Transactional
    void getLandPhoto() throws Exception {
        // Initialize the database
        landPhotoRepository.saveAndFlush(landPhoto);

        // Get the landPhoto
        restLandPhotoMockMvc
            .perform(get(ENTITY_API_URL_ID, landPhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(landPhoto.getId().intValue()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL));
    }

    @Test
    @Transactional
    void getNonExistingLandPhoto() throws Exception {
        // Get the landPhoto
        restLandPhotoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLandPhoto() throws Exception {
        // Initialize the database
        landPhotoRepository.saveAndFlush(landPhoto);

        int databaseSizeBeforeUpdate = landPhotoRepository.findAll().size();

        // Update the landPhoto
        LandPhoto updatedLandPhoto = landPhotoRepository.findById(landPhoto.getId()).get();
        // Disconnect from session so that the updates on updatedLandPhoto are not directly saved in db
        em.detach(updatedLandPhoto);
        updatedLandPhoto.imageUrl(UPDATED_IMAGE_URL);

        restLandPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLandPhoto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLandPhoto))
            )
            .andExpect(status().isOk());

        // Validate the LandPhoto in the database
        List<LandPhoto> landPhotoList = landPhotoRepository.findAll();
        assertThat(landPhotoList).hasSize(databaseSizeBeforeUpdate);
        LandPhoto testLandPhoto = landPhotoList.get(landPhotoList.size() - 1);
        assertThat(testLandPhoto.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void putNonExistingLandPhoto() throws Exception {
        int databaseSizeBeforeUpdate = landPhotoRepository.findAll().size();
        landPhoto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLandPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, landPhoto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landPhoto))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandPhoto in the database
        List<LandPhoto> landPhotoList = landPhotoRepository.findAll();
        assertThat(landPhotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLandPhoto() throws Exception {
        int databaseSizeBeforeUpdate = landPhotoRepository.findAll().size();
        landPhoto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landPhoto))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandPhoto in the database
        List<LandPhoto> landPhotoList = landPhotoRepository.findAll();
        assertThat(landPhotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLandPhoto() throws Exception {
        int databaseSizeBeforeUpdate = landPhotoRepository.findAll().size();
        landPhoto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandPhotoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landPhoto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LandPhoto in the database
        List<LandPhoto> landPhotoList = landPhotoRepository.findAll();
        assertThat(landPhotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLandPhotoWithPatch() throws Exception {
        // Initialize the database
        landPhotoRepository.saveAndFlush(landPhoto);

        int databaseSizeBeforeUpdate = landPhotoRepository.findAll().size();

        // Update the landPhoto using partial update
        LandPhoto partialUpdatedLandPhoto = new LandPhoto();
        partialUpdatedLandPhoto.setId(landPhoto.getId());

        partialUpdatedLandPhoto.imageUrl(UPDATED_IMAGE_URL);

        restLandPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLandPhoto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLandPhoto))
            )
            .andExpect(status().isOk());

        // Validate the LandPhoto in the database
        List<LandPhoto> landPhotoList = landPhotoRepository.findAll();
        assertThat(landPhotoList).hasSize(databaseSizeBeforeUpdate);
        LandPhoto testLandPhoto = landPhotoList.get(landPhotoList.size() - 1);
        assertThat(testLandPhoto.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void fullUpdateLandPhotoWithPatch() throws Exception {
        // Initialize the database
        landPhotoRepository.saveAndFlush(landPhoto);

        int databaseSizeBeforeUpdate = landPhotoRepository.findAll().size();

        // Update the landPhoto using partial update
        LandPhoto partialUpdatedLandPhoto = new LandPhoto();
        partialUpdatedLandPhoto.setId(landPhoto.getId());

        partialUpdatedLandPhoto.imageUrl(UPDATED_IMAGE_URL);

        restLandPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLandPhoto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLandPhoto))
            )
            .andExpect(status().isOk());

        // Validate the LandPhoto in the database
        List<LandPhoto> landPhotoList = landPhotoRepository.findAll();
        assertThat(landPhotoList).hasSize(databaseSizeBeforeUpdate);
        LandPhoto testLandPhoto = landPhotoList.get(landPhotoList.size() - 1);
        assertThat(testLandPhoto.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void patchNonExistingLandPhoto() throws Exception {
        int databaseSizeBeforeUpdate = landPhotoRepository.findAll().size();
        landPhoto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLandPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, landPhoto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(landPhoto))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandPhoto in the database
        List<LandPhoto> landPhotoList = landPhotoRepository.findAll();
        assertThat(landPhotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLandPhoto() throws Exception {
        int databaseSizeBeforeUpdate = landPhotoRepository.findAll().size();
        landPhoto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(landPhoto))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandPhoto in the database
        List<LandPhoto> landPhotoList = landPhotoRepository.findAll();
        assertThat(landPhotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLandPhoto() throws Exception {
        int databaseSizeBeforeUpdate = landPhotoRepository.findAll().size();
        landPhoto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(landPhoto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LandPhoto in the database
        List<LandPhoto> landPhotoList = landPhotoRepository.findAll();
        assertThat(landPhotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLandPhoto() throws Exception {
        // Initialize the database
        landPhotoRepository.saveAndFlush(landPhoto);

        int databaseSizeBeforeDelete = landPhotoRepository.findAll().size();

        // Delete the landPhoto
        restLandPhotoMockMvc
            .perform(delete(ENTITY_API_URL_ID, landPhoto.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LandPhoto> landPhotoList = landPhotoRepository.findAll();
        assertThat(landPhotoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
