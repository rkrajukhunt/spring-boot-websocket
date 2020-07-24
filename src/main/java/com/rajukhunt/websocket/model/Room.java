package com.rajukhunt.websocket.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.rajukhunt.websocket.bean.GroupBean;

@Entity
@Table(name = "chat_room")
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;

	@Enumerated(EnumType.STRING)
	private RoomType type;

	@ManyToOne
	@JoinColumn(name = "created_by")
	private User createdBy;

	@OneToMany(mappedBy = "room", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Occupants> occupants;

	public enum RoomType {
		SINGLE, GROUP
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public RoomType getType() {
		return type;
	}

	public void setType(RoomType type) {
		this.type = type;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public List<Occupants> getOccupants() {
		return occupants;
	}

	public void setOccupants(List<Occupants> occupants) {
		this.occupants = occupants;
	}

	public GroupBean toBean() {
		GroupBean bean = new GroupBean();
		bean.setTitle(this.title);
		bean.setId(this.id);
		bean.setCreator(this.createdBy.toBean());
		return bean;
	}

}
