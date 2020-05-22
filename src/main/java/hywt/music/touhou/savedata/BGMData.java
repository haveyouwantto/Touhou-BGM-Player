package hywt.music.touhou.savedata;

import java.util.Arrays;
import java.util.List;

public class BGMData {
    // TODO auto load order
    public int[] date;
    public List<Game> games;
    public List<String> comments;
    public List<String> order;

    public BGMData(int[] date, List<Game> games) {
        super();
        this.date = date;
        this.games = games;
    }

    public Game getGamebyId(String id) {
        return games.get(order.indexOf(id.toUpperCase()));
    }

    public Music getMusicbyId(String id) {
        String[] ids = id.toUpperCase().split("_");
        return getGamebyId(ids[0]).music.get(Integer.parseInt(ids[1]) - 1);
    }

    @Override
    public String toString() {
        return "BGMData [date=" + Arrays.toString(date) + ", games=" + games + ", comments=" + comments + "]";
    }

    public List<String> getComments() {
        return comments;
    }

    public int[] indexOf(Music m) {
        for (int i = 0; i < games.size(); i++) {
            int index = games.get(i).indexOf(m);
            if (index != -1) {
                return new int[]{i, index};
            }
        }
        return null;
    }

    public Game getGamebyMusic(Music m) {
        for (Game game : games) {
            int index = game.indexOf(m);
            if (index != -1) return game;
        }
        return null;
    }
}
