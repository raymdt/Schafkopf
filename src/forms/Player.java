package forms;

import java.util.ArrayList;
import java.util.List;

public class Player {
	
	private List<Karte> playerKarten = new ArrayList<Karte>();
	private int Score;
	private final String name;
	private int id;
	private boolean hasPlayOnThisRund;
	
	public Player(String name) {
		this.name = name;
		this.hasPlayOnThisRund = false;
	}

	public List<Karte> getPlayerKarten() {
		return playerKarten;
	}

	public void setPlayerKarten(List<Karte> playerKarten) {
		this.playerKarten = playerKarten;
	}

	public int getScore() {
		return Score;
	}

	public void setScore(int score) {
		Score = score;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isHasPlayOnThisRund() {
		return hasPlayOnThisRund;
	}

	public void setHasPlayOnThisRund(boolean hasPlayOnThisRund) {
		this.hasPlayOnThisRund = hasPlayOnThisRund;
	}
	
	
	
}
