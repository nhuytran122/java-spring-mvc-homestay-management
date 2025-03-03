package com.lullabyhomestay.homestay_management.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class IconService {
    private final WebClient webClient;
    private List<String> cachedIconList;

    private Mono<List<String>> getIconListFromApi() {
        String apiUrl = "https://raw.githubusercontent.com/iconify/icon-sets/refs/heads/master/json/mdi.json";
        String provider = "mdi";

        return webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(String.class) // Lấy raw string vì API trả text/plain
                .timeout(Duration.ofSeconds(10))
                .map(response -> parseIconsFromJson(response, provider))
                .onErrorResume(e -> {
                    System.err.println("Lỗi khi gọi API: " + e.getMessage());
                    return Mono.just(new ArrayList<>());
                });
    }

    @SuppressWarnings("unchecked")
    private List<String> parseIconsFromJson(String jsonResponse, String provider) {
        List<String> icons = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);
            Map<String, Object> iconsMap = (Map<String, Object>) responseMap.get("icons");

            if (iconsMap != null && !iconsMap.isEmpty()) {
                for (String iconName : iconsMap.keySet()) {
                    icons.add(provider + ":" + iconName);
                }
            } else {
                System.err.println("No result for " + provider);
            }
        } catch (Exception e) {
            System.err.println("Error when parsing JSON: " + e.getMessage());
        }
        return icons;
    }

    @PostConstruct
    public void init() {
        getIconListFromApi()
                .switchIfEmpty(Mono.just(Arrays.asList("mdi:bed", "mdi:wifi", "mdi:swimming-pool")))
                .subscribe(icons -> {
                    this.cachedIconList = icons;
                });
    }

    // @PostConstruct
    // public void init() {
    // Mono<List<String>> iconMono = getIconListFromApi();
    // cachedIconList = iconMono.switchIfEmpty(Mono.just(Arrays.asList("mdi:bed",
    // "mdi:wifi", "mdi:swimming-pool")))
    // .block();
    // }

    public List<String> getCachedIconList() {
        return cachedIconList;
    }
}