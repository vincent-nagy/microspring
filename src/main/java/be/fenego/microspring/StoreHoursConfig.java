package be.fenego.microspring;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@PropertySource("classpath:standard-hours.properties")
@ConfigurationProperties("standard-opening-hours")
@EnableJdbcRepositories
public class StoreHoursConfig {
	private List<String> dayOfWeek;
	private List<String> openingTime;
	private List<String> closingTime;
	
	public List<String> getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(List<String> dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public List<String> getOpeningTime() {
		return openingTime;
	}
	public void setOpeningTime(List<String> openingTime) {
		this.openingTime = openingTime;
	}
	public List<String> getClosingTime() {
		return closingTime;
	}
	public void setClosingTime(List<String> closingTime) {
		this.closingTime = closingTime;
	}
	
	
}
