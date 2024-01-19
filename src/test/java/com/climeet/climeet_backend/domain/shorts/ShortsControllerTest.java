package com.climeet.climeet_backend.domain.shorts;

import com.climeet.climeet_backend.domain.climber.ClimberRepository;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.route.RouteRepository;
import com.climeet.climeet_backend.domain.sector.Sector;
import com.climeet.climeet_backend.domain.sector.SectorRepository;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("dev")
class ShortsControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ShortsRepository shortsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClimberRepository climberRepository;

    @Autowired
    ClimbingGymRepository climbingGymRepository;

    @Autowired
    SectorRepository sectorRepository;

    @Autowired
    RouteRepository routeRepository;

    @BeforeAll
    void beforeAll() {
        Optional<User> user = userRepository.findById(1L);
        Optional<ClimbingGym> climbingGym = climbingGymRepository.findById(1L);
        Optional<Route> route = routeRepository.findById(1L);
        Sector sector = route.get().getSector();

        List<Shorts> shortsList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Shorts shorts = Shorts.builder()
                .climbingGym(climbingGym.get())
                .sector(sector)
                .route(route.get())
                .user(user.get())
                .build();

            shortsList.add(shorts);
        }

        shortsRepository.saveAll(shortsList);
    }

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .build();
    }

    @AfterEach
    public void AfterEach() {
        Pageable pageable = PageRequest.of(0, 5);
        Slice<Shorts> shortsSlice = shortsRepository.findAllByOrderByCreatedAtDesc(pageable);
        shortsRepository.deleteAll(shortsSlice.getContent());
    }

    @DisplayName("숏츠 최신순 조회에 성공한다")
    @Test
    public void findLatestShorts() throws Exception {
        //given
        final String url = "/shorts/latest?page=2&size=2";

        //when
        final ResultActions resultActions = mockMvc.perform(get(url).accept(
            MediaType.APPLICATION_JSON));

        //then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.page").value(2))
            .andExpect(jsonPath("$.result.hasNext").value(true));

    }

    @DisplayName("숏츠 인기순 조회에 성공한다")
    @Test
    void findPopularShorts() throws Exception {
        //given
        final String url = "/shorts/popular?page=0&size=2";
        Pageable pageable = PageRequest.of(0, 5);
        Shorts shorts = shortsRepository.findAllByRankingNotZeroOrderByRankingAscCreatedAtDesc(pageable).getContent().get(0);
        ClimbingGym climbingGym = climbingGymRepository.findById(shorts.getClimbingGym().getId()).get();
        Hibernate.initialize(climbingGym);
        String expectedHighestRankingShortsGymName = climbingGym.getName();

        //when
        final ResultActions resultActions = mockMvc.perform(get(url).accept(
            MediaType.APPLICATION_JSON));

        //then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.result[0].gymName").value(expectedHighestRankingShortsGymName));
    }
}