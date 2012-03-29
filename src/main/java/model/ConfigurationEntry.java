package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ConfigurationEntry {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Project project;
	@Column(name = "entry_key")
	private String key;
	@Column(name = "entry_value")
	private String value;

	public ConfigurationEntry() {
	}

	public ConfigurationEntry(String key, String value, Project project) {
		this.key = key;
		this.value = value;
		this.project = project;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

}
