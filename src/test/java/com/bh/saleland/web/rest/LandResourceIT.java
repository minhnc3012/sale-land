package com.bh.saleland.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bh.saleland.IntegrationTest;
import com.bh.saleland.domain.Land;
import com.bh.saleland.domain.enumeration.FeeType;
import com.bh.saleland.domain.enumeration.LandStatus;
import com.bh.saleland.domain.enumeration.LandType;
import com.bh.saleland.domain.enumeration.PriceType;
import com.bh.saleland.domain.enumeration.UnitPriceType;
import com.bh.saleland.repository.LandRepository;
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
 * Integration tests for the {@link LandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LandResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final LandStatus DEFAULT_STATUS = LandStatus.A;
    private static final LandStatus UPDATED_STATUS = LandStatus.A;

    private static final LandType DEFAULT_TYPE = LandType.A;
    private static final LandType UPDATED_TYPE = LandType.A;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final UnitPriceType DEFAULT_INIT = UnitPriceType.A;
    private static final UnitPriceType UPDATED_INIT = UnitPriceType.A;

    private static final PriceType DEFAULT_PRICE_TYPE = PriceType.A;
    private static final PriceType UPDATED_PRICE_TYPE = PriceType.A;

    private static final FeeType DEFAULT_FEE_TYPE = FeeType.A;
    private static final FeeType UPDATED_FEE_TYPE = FeeType.A;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_WIDTH = 1D;
    private static final Double UPDATED_WIDTH = 2D;

    private static final Double DEFAULT_HEIGHT = 1D;
    private static final Double UPDATED_HEIGHT = 2D;

    private static final Double DEFAULT_AREA = 1D;
    private static final Double UPDATED_AREA = 2D;

    private static final String ENTITY_API_URL = "/api/lands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LandRepository landRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLandMockMvc;

    private Land land;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Land createEntity(EntityManager em) {
        Land land = new Land()
            .title(DEFAULT_TITLE)
            .address(DEFAULT_ADDRESS)
            .status(DEFAULT_STATUS)
            .type(DEFAULT_TYPE)
            .price(DEFAULT_PRICE)
            .init(DEFAULT_INIT)
            .priceType(DEFAULT_PRICE_TYPE)
            .feeType(DEFAULT_FEE_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .width(DEFAULT_WIDTH)
            .height(DEFAULT_HEIGHT)
            .area(DEFAULT_AREA);
        return land;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Land createUpdatedEntity(EntityManager em) {
        Land land = new Land()
            .title(UPDATED_TITLE)
            .address(UPDATED_ADDRESS)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .price(UPDATED_PRICE)
            .init(UPDATED_INIT)
            .priceType(UPDATED_PRICE_TYPE)
            .feeType(UPDATED_FEE_TYPE)
            .description(UPDATED_DESCRIPTION)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .area(UPDATED_AREA);
        return land;
    }

    @BeforeEach
    public void initTest() {
        land = createEntity(em);
    }

    @Test
    @Transactional
    void createLand() throws Exception {
        int databaseSizeBeforeCreate = landRepository.findAll().size();
        // Create the Land
        restLandMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(land))
            )
            .andExpect(status().isCreated());

        // Validate the Land in the database
        List<Land> landList = landRepository.findAll();
        assertThat(landList).hasSize(databaseSizeBeforeCreate + 1);
        Land testLand = landList.get(landList.size() - 1);
        assertThat(testLand.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testLand.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testLand.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLand.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testLand.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testLand.getInit()).isEqualTo(DEFAULT_INIT);
        assertThat(testLand.getPriceType()).isEqualTo(DEFAULT_PRICE_TYPE);
        assertThat(testLand.getFeeType()).isEqualTo(DEFAULT_FEE_TYPE);
        assertThat(testLand.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLand.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testLand.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testLand.getArea()).isEqualTo(DEFAULT_AREA);
    }

    @Test
    @Transactional
    void createLandWithExistingId() throws Exception {
        // Create the Land with an existing ID
        land.setId(1L);

        int databaseSizeBeforeCreate = landRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLandMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(land))
            )
            .andExpect(status().isBadRequest());

        // Validate the Land in the database
        List<Land> landList = landRepository.findAll();
        assertThat(landList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLands() throws Exception {
        // Initialize the database
        landRepository.saveAndFlush(land);

        // Get all the landList
        restLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(land.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].init").value(hasItem(DEFAULT_INIT.toString())))
            .andExpect(jsonPath("$.[*].priceType").value(hasItem(DEFAULT_PRICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].feeType").value(hasItem(DEFAULT_FEE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH.doubleValue())))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.doubleValue())));
    }

    @Test
    @Transactional
    void getLand() throws Exception {
        // Initialize the database
        landRepository.saveAndFlush(land);

        // Get the land
        restLandMockMvc
            .perform(get(ENTITY_API_URL_ID, land.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(land.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.init").value(DEFAULT_INIT.toString()))
            .andExpect(jsonPath("$.priceType").value(DEFAULT_PRICE_TYPE.toString()))
            .andExpect(jsonPath("$.feeType").value(DEFAULT_FEE_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH.doubleValue()))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.doubleValue()))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingLand() throws Exception {
        // Get the land
        restLandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLand() throws Exception {
        // Initialize the database
        landRepository.saveAndFlush(land);

        int databaseSizeBeforeUpdate = landRepository.findAll().size();

        // Update the land
        Land updatedLand = landRepository.findById(land.getId()).get();
        // Disconnect from session so that the updates on updatedLand are not directly saved in db
        em.detach(updatedLand);
        updatedLand
            .title(UPDATED_TITLE)
            .address(UPDATED_ADDRESS)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .price(UPDATED_PRICE)
            .init(UPDATED_INIT)
            .priceType(UPDATED_PRICE_TYPE)
            .feeType(UPDATED_FEE_TYPE)
            .description(UPDATED_DESCRIPTION)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .area(UPDATED_AREA);

        restLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLand.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLand))
            )
            .andExpect(status().isOk());

        // Validate the Land in the database
        List<Land> landList = landRepository.findAll();
        assertThat(landList).hasSize(databaseSizeBeforeUpdate);
        Land testLand = landList.get(landList.size() - 1);
        assertThat(testLand.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testLand.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testLand.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLand.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testLand.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testLand.getInit()).isEqualTo(UPDATED_INIT);
        assertThat(testLand.getPriceType()).isEqualTo(UPDATED_PRICE_TYPE);
        assertThat(testLand.getFeeType()).isEqualTo(UPDATED_FEE_TYPE);
        assertThat(testLand.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLand.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testLand.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testLand.getArea()).isEqualTo(UPDATED_AREA);
    }

    @Test
    @Transactional
    void putNonExistingLand() throws Exception {
        int databaseSizeBeforeUpdate = landRepository.findAll().size();
        land.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, land.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(land))
            )
            .andExpect(status().isBadRequest());

        // Validate the Land in the database
        List<Land> landList = landRepository.findAll();
        assertThat(landList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLand() throws Exception {
        int databaseSizeBeforeUpdate = landRepository.findAll().size();
        land.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(land))
            )
            .andExpect(status().isBadRequest());

        // Validate the Land in the database
        List<Land> landList = landRepository.findAll();
        assertThat(landList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLand() throws Exception {
        int databaseSizeBeforeUpdate = landRepository.findAll().size();
        land.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(land))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Land in the database
        List<Land> landList = landRepository.findAll();
        assertThat(landList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLandWithPatch() throws Exception {
        // Initialize the database
        landRepository.saveAndFlush(land);

        int databaseSizeBeforeUpdate = landRepository.findAll().size();

        // Update the land using partial update
        Land partialUpdatedLand = new Land();
        partialUpdatedLand.setId(land.getId());

        partialUpdatedLand
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .price(UPDATED_PRICE)
            .init(UPDATED_INIT)
            .priceType(UPDATED_PRICE_TYPE)
            .feeType(UPDATED_FEE_TYPE)
            .description(UPDATED_DESCRIPTION)
            .height(UPDATED_HEIGHT);

        restLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLand))
            )
            .andExpect(status().isOk());

        // Validate the Land in the database
        List<Land> landList = landRepository.findAll();
        assertThat(landList).hasSize(databaseSizeBeforeUpdate);
        Land testLand = landList.get(landList.size() - 1);
        assertThat(testLand.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testLand.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testLand.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLand.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testLand.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testLand.getInit()).isEqualTo(UPDATED_INIT);
        assertThat(testLand.getPriceType()).isEqualTo(UPDATED_PRICE_TYPE);
        assertThat(testLand.getFeeType()).isEqualTo(UPDATED_FEE_TYPE);
        assertThat(testLand.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLand.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testLand.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testLand.getArea()).isEqualTo(DEFAULT_AREA);
    }

    @Test
    @Transactional
    void fullUpdateLandWithPatch() throws Exception {
        // Initialize the database
        landRepository.saveAndFlush(land);

        int databaseSizeBeforeUpdate = landRepository.findAll().size();

        // Update the land using partial update
        Land partialUpdatedLand = new Land();
        partialUpdatedLand.setId(land.getId());

        partialUpdatedLand
            .title(UPDATED_TITLE)
            .address(UPDATED_ADDRESS)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .price(UPDATED_PRICE)
            .init(UPDATED_INIT)
            .priceType(UPDATED_PRICE_TYPE)
            .feeType(UPDATED_FEE_TYPE)
            .description(UPDATED_DESCRIPTION)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .area(UPDATED_AREA);

        restLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLand))
            )
            .andExpect(status().isOk());

        // Validate the Land in the database
        List<Land> landList = landRepository.findAll();
        assertThat(landList).hasSize(databaseSizeBeforeUpdate);
        Land testLand = landList.get(landList.size() - 1);
        assertThat(testLand.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testLand.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testLand.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLand.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testLand.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testLand.getInit()).isEqualTo(UPDATED_INIT);
        assertThat(testLand.getPriceType()).isEqualTo(UPDATED_PRICE_TYPE);
        assertThat(testLand.getFeeType()).isEqualTo(UPDATED_FEE_TYPE);
        assertThat(testLand.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLand.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testLand.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testLand.getArea()).isEqualTo(UPDATED_AREA);
    }

    @Test
    @Transactional
    void patchNonExistingLand() throws Exception {
        int databaseSizeBeforeUpdate = landRepository.findAll().size();
        land.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, land.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(land))
            )
            .andExpect(status().isBadRequest());

        // Validate the Land in the database
        List<Land> landList = landRepository.findAll();
        assertThat(landList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLand() throws Exception {
        int databaseSizeBeforeUpdate = landRepository.findAll().size();
        land.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(land))
            )
            .andExpect(status().isBadRequest());

        // Validate the Land in the database
        List<Land> landList = landRepository.findAll();
        assertThat(landList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLand() throws Exception {
        int databaseSizeBeforeUpdate = landRepository.findAll().size();
        land.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(land))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Land in the database
        List<Land> landList = landRepository.findAll();
        assertThat(landList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLand() throws Exception {
        // Initialize the database
        landRepository.saveAndFlush(land);

        int databaseSizeBeforeDelete = landRepository.findAll().size();

        // Delete the land
        restLandMockMvc
            .perform(delete(ENTITY_API_URL_ID, land.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Land> landList = landRepository.findAll();
        assertThat(landList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
