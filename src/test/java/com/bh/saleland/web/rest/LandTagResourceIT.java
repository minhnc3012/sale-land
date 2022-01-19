package com.bh.saleland.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bh.saleland.IntegrationTest;
import com.bh.saleland.domain.LandTag;
import com.bh.saleland.repository.LandTagRepository;
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
 * Integration tests for the {@link LandTagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LandTagResourceIT {

    private static final String DEFAULT_TAG = "AAAAAAAAAA";
    private static final String UPDATED_TAG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/land-tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LandTagRepository landTagRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLandTagMockMvc;

    private LandTag landTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LandTag createEntity(EntityManager em) {
        LandTag landTag = new LandTag().tag(DEFAULT_TAG);
        return landTag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LandTag createUpdatedEntity(EntityManager em) {
        LandTag landTag = new LandTag().tag(UPDATED_TAG);
        return landTag;
    }

    @BeforeEach
    public void initTest() {
        landTag = createEntity(em);
    }

    @Test
    @Transactional
    void createLandTag() throws Exception {
        int databaseSizeBeforeCreate = landTagRepository.findAll().size();
        // Create the LandTag
        restLandTagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landTag))
            )
            .andExpect(status().isCreated());

        // Validate the LandTag in the database
        List<LandTag> landTagList = landTagRepository.findAll();
        assertThat(landTagList).hasSize(databaseSizeBeforeCreate + 1);
        LandTag testLandTag = landTagList.get(landTagList.size() - 1);
        assertThat(testLandTag.getTag()).isEqualTo(DEFAULT_TAG);
    }

    @Test
    @Transactional
    void createLandTagWithExistingId() throws Exception {
        // Create the LandTag with an existing ID
        landTag.setId(1L);

        int databaseSizeBeforeCreate = landTagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLandTagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landTag))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandTag in the database
        List<LandTag> landTagList = landTagRepository.findAll();
        assertThat(landTagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLandTags() throws Exception {
        // Initialize the database
        landTagRepository.saveAndFlush(landTag);

        // Get all the landTagList
        restLandTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(landTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG)));
    }

    @Test
    @Transactional
    void getLandTag() throws Exception {
        // Initialize the database
        landTagRepository.saveAndFlush(landTag);

        // Get the landTag
        restLandTagMockMvc
            .perform(get(ENTITY_API_URL_ID, landTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(landTag.getId().intValue()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG));
    }

    @Test
    @Transactional
    void getNonExistingLandTag() throws Exception {
        // Get the landTag
        restLandTagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLandTag() throws Exception {
        // Initialize the database
        landTagRepository.saveAndFlush(landTag);

        int databaseSizeBeforeUpdate = landTagRepository.findAll().size();

        // Update the landTag
        LandTag updatedLandTag = landTagRepository.findById(landTag.getId()).get();
        // Disconnect from session so that the updates on updatedLandTag are not directly saved in db
        em.detach(updatedLandTag);
        updatedLandTag.tag(UPDATED_TAG);

        restLandTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLandTag.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLandTag))
            )
            .andExpect(status().isOk());

        // Validate the LandTag in the database
        List<LandTag> landTagList = landTagRepository.findAll();
        assertThat(landTagList).hasSize(databaseSizeBeforeUpdate);
        LandTag testLandTag = landTagList.get(landTagList.size() - 1);
        assertThat(testLandTag.getTag()).isEqualTo(UPDATED_TAG);
    }

    @Test
    @Transactional
    void putNonExistingLandTag() throws Exception {
        int databaseSizeBeforeUpdate = landTagRepository.findAll().size();
        landTag.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLandTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, landTag.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landTag))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandTag in the database
        List<LandTag> landTagList = landTagRepository.findAll();
        assertThat(landTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLandTag() throws Exception {
        int databaseSizeBeforeUpdate = landTagRepository.findAll().size();
        landTag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landTag))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandTag in the database
        List<LandTag> landTagList = landTagRepository.findAll();
        assertThat(landTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLandTag() throws Exception {
        int databaseSizeBeforeUpdate = landTagRepository.findAll().size();
        landTag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandTagMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(landTag))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LandTag in the database
        List<LandTag> landTagList = landTagRepository.findAll();
        assertThat(landTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLandTagWithPatch() throws Exception {
        // Initialize the database
        landTagRepository.saveAndFlush(landTag);

        int databaseSizeBeforeUpdate = landTagRepository.findAll().size();

        // Update the landTag using partial update
        LandTag partialUpdatedLandTag = new LandTag();
        partialUpdatedLandTag.setId(landTag.getId());

        partialUpdatedLandTag.tag(UPDATED_TAG);

        restLandTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLandTag.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLandTag))
            )
            .andExpect(status().isOk());

        // Validate the LandTag in the database
        List<LandTag> landTagList = landTagRepository.findAll();
        assertThat(landTagList).hasSize(databaseSizeBeforeUpdate);
        LandTag testLandTag = landTagList.get(landTagList.size() - 1);
        assertThat(testLandTag.getTag()).isEqualTo(UPDATED_TAG);
    }

    @Test
    @Transactional
    void fullUpdateLandTagWithPatch() throws Exception {
        // Initialize the database
        landTagRepository.saveAndFlush(landTag);

        int databaseSizeBeforeUpdate = landTagRepository.findAll().size();

        // Update the landTag using partial update
        LandTag partialUpdatedLandTag = new LandTag();
        partialUpdatedLandTag.setId(landTag.getId());

        partialUpdatedLandTag.tag(UPDATED_TAG);

        restLandTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLandTag.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLandTag))
            )
            .andExpect(status().isOk());

        // Validate the LandTag in the database
        List<LandTag> landTagList = landTagRepository.findAll();
        assertThat(landTagList).hasSize(databaseSizeBeforeUpdate);
        LandTag testLandTag = landTagList.get(landTagList.size() - 1);
        assertThat(testLandTag.getTag()).isEqualTo(UPDATED_TAG);
    }

    @Test
    @Transactional
    void patchNonExistingLandTag() throws Exception {
        int databaseSizeBeforeUpdate = landTagRepository.findAll().size();
        landTag.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLandTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, landTag.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(landTag))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandTag in the database
        List<LandTag> landTagList = landTagRepository.findAll();
        assertThat(landTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLandTag() throws Exception {
        int databaseSizeBeforeUpdate = landTagRepository.findAll().size();
        landTag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(landTag))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandTag in the database
        List<LandTag> landTagList = landTagRepository.findAll();
        assertThat(landTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLandTag() throws Exception {
        int databaseSizeBeforeUpdate = landTagRepository.findAll().size();
        landTag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandTagMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(landTag))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LandTag in the database
        List<LandTag> landTagList = landTagRepository.findAll();
        assertThat(landTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLandTag() throws Exception {
        // Initialize the database
        landTagRepository.saveAndFlush(landTag);

        int databaseSizeBeforeDelete = landTagRepository.findAll().size();

        // Delete the landTag
        restLandTagMockMvc
            .perform(delete(ENTITY_API_URL_ID, landTag.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LandTag> landTagList = landTagRepository.findAll();
        assertThat(landTagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
