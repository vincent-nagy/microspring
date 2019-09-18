package be.fenego.microspring.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import be.fenego.microspring.StoreHoursConfig;
import be.fenego.microspring.dtos.EnrichedStore;
import be.fenego.microspring.dtos.Store;
import be.fenego.microspring.dtos.StoreWrapper;
import be.fenego.microspring.entities.StoreHours;
import be.fenego.microspring.repositories.StoreHoursRepository;

public class StoreService {
	@Autowired
	private StoreHoursRepository storeHoursRepository;
	@Autowired
	private StoreHoursConfig storeHoursConfig;
	@Autowired
	protected RestTemplate restTemplate;

	protected String serviceUrl;

	public StoreService(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl : "http://" + serviceUrl;
	}

	public List<EnrichedStore> getAllStores() {
		StoreWrapper storeWrapper = restTemplate.getForObject(serviceUrl, StoreWrapper.class);
		Store[] stores = storeWrapper.getElements();

		List<EnrichedStore> enrichedStores = Arrays.stream(stores).map(EnrichedStore::new).collect(Collectors.toList());

		LocalDate lowerLimit = LocalDate.now().with(DayOfWeek.MONDAY);
		LocalDate upperLimit = LocalDate.now().plusWeeks(1).with(DayOfWeek.SUNDAY);

		List<StoreHours> storeHours = storeHoursRepository.findByDateRange(lowerLimit, upperLimit,
				Arrays.stream(stores).map(Store::getUuid).collect(Collectors.toList()));

		enrichedStores.forEach(x -> fillInOpeningHours(x, storeHours));
		
		return enrichedStores;
	}
	
	
	
	private EnrichedStore fillInOpeningHours(EnrichedStore enrichedStore, List<StoreHours> storeHours) {
		List<StoreHours> completeStoreHours = new ArrayList<>();
		LocalDate lowerLimit = LocalDate.now().with(DayOfWeek.MONDAY);

		List<String> openingTime = storeHoursConfig.getOpeningTime();
		List<String> closingTime = storeHoursConfig.getClosingTime();
		
		for (int i = 0; i < 14; i++) {
			
			String standardOpeningTime = openingTime.get(i % 7);
			String standardClosingTime = closingTime.get(i % 7);

			StoreHours standard = new StoreHours(lowerLimit.plus(i, ChronoUnit.DAYS).toString(), standardOpeningTime,
					standardClosingTime);
			completeStoreHours.add(standard);
		}

		if (storeHours != null && !storeHours.isEmpty()) {
			List<String> divergentDates = storeHours.stream().map(x -> x.getDate()).collect(Collectors.toList());

			completeStoreHours.removeIf(x -> divergentDates.contains(x.getDate()));
			completeStoreHours.addAll(storeHours);
		}

		enrichedStore.setOpeningHours(completeStoreHours);

		return enrichedStore;
	}
}
