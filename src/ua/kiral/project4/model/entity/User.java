package ua.kiral.project4.model.entity;

import java.io.Serializable;

/**
 * Simple container of user data.
 *
 */
public class User implements Serializable {
	private Integer userId;
	private String name;
	private String surname;
	private String login;
	private String password;
	private String email;
	/*
	 * true = male, false = female
	 */
	private Boolean male;
	private Boolean blocked;
	private Boolean manager;

	public User() {
	}

	public User(Integer userId, String name, String surname, String login, String password, String email, Boolean male,
			Boolean blocked, Boolean manager) {
		this.userId = userId;
		this.name = name;
		this.surname = surname;
		this.login = login;
		this.password = password;
		this.email = email;
		this.male = male;
		this.blocked = blocked;
		this.manager = manager;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getMale() {
		return male;
	}

	public void setMale(Boolean male) {
		this.male = male;
	}

	public Boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(Boolean isBlocked) {
		this.blocked = isBlocked;
	}

	public Boolean getManager() {
		return manager;
	}

	public void setManager(Boolean manager) {
		this.manager = manager;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User userId=").append(userId).append(", name=").append(name).append(", surname=")
				.append(surname).append(", login=").append(login).append(", password=").append(password)
				.append(", email=").append(email).append(", male=").append(male).append(", blocked=").append(blocked)
				.append(", manager=").append(manager);
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blocked == null) ? 0 : blocked.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((male == null) ? 0 : male.hashCode());
		result = prime * result + ((manager == null) ? 0 : manager.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (blocked == null) {
			if (other.blocked != null)
				return false;
		} else if (!blocked.equals(other.blocked))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (male == null) {
			if (other.male != null)
				return false;
		} else if (!male.equals(other.male))
			return false;
		if (manager == null) {
			if (other.manager != null)
				return false;
		} else if (!manager.equals(other.manager))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
}