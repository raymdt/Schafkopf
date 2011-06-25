package forms;

import java.util.ArrayList;
import java.util.List;

public class Player {

	private List<Karte> playerKarten = new ArrayList<Karte>();
	private int Score;
	private final String name;
	
	public Player(String name) {
		this.name = name;
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
	
	
	
}
