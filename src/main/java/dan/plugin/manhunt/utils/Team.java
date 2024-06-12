package dan.plugin.manhunt.utils;

import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Team implements List<Player> {
    private final List<Player> players;
    private int playerLimit = Integer.MAX_VALUE;
    private String teamName = "NO NAME MADE";

    public Team(String teamName) {
        this.teamName = teamName;
        this.players = new ArrayList<>();
    }

    public Team(String teamName, int playerLimit) {
        this.teamName = teamName;
        this.playerLimit = playerLimit;
        this.players = new ArrayList<>();
    }

    public Set<Player> getPlayers() {
        synchronized (players) {
            return Collections.unmodifiableSet(new HashSet<>(players));
        }
    }


    @Override
    public boolean add(Player player) {
        synchronized (players) {
            if (players.size() < playerLimit) {
                return players.add(player);
            }
        }
        return false;
    }

    @Override
    public void add(int index, Player player) {
        synchronized (players) {
            if (players.size() < playerLimit) {
                players.add(index, player);
            }
        }
    }

    @Override
    public boolean addAll(Collection<? extends Player> c) {
        synchronized (players) {
            if (players.size() + c.size() <= playerLimit) {
                return players.addAll(c);
            }
        }
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Player> c) {
        synchronized (players) {
            if (players.size() + c.size() <= playerLimit) {
                return players.addAll(index, c);
            }
        }
        return false;
    }

    // Other List methods delegate to the internal list
    @Override
    public int size() {
        synchronized (players) {
            return players.size();
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized (players) {
            return players.isEmpty();
        }
    }

    @Override
    public boolean contains(Object o) {
        synchronized (players) {
            return players.contains(o);
        }
    }

    @Override
    public Iterator<Player> iterator() {
        return players.iterator(); // Should be synchronized externally if used
    }

    @Override
    public Object[] toArray() {
        synchronized (players) {
            return players.toArray();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        synchronized (players) {
            return players.toArray(a);
        }
    }

    @Override
    public boolean remove(Object o) {
        synchronized (players) {
            return players.remove(o);
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        synchronized (players) {
            return players.containsAll(c);
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        synchronized (players) {
            return players.removeAll(c);
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        synchronized (players) {
            return players.retainAll(c);
        }
    }

    @Override
    public void clear() {
        synchronized (players) {
            players.clear();
        }
    }

    @Override
    public Player get(int index) {
        synchronized (players) {
            return players.get(index);
        }
    }

    @Override
    public Player set(int index, Player element) {
        synchronized (players) {
            return players.set(index, element);
        }
    }

    @Override
    public Player remove(int index) {
        synchronized (players) {
            return players.remove(index);
        }
    }

    @Override
    public int indexOf(Object o) {
        synchronized (players) {
            return players.indexOf(o);
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        synchronized (players) {
            return players.lastIndexOf(o);
        }
    }

    @Override
    public ListIterator<Player> listIterator() {
        synchronized (players){
            return players.listIterator();
        }
    }

    @Override
    public ListIterator<Player> listIterator(int index) {
        synchronized (players){
            return players.listIterator(index);
        } // Should be synchronized externally if used
    }

    @Override
    public List<Player> subList(int fromIndex, int toIndex) {
        synchronized (players) {
            return players.subList(fromIndex, toIndex);
        }
    }

    public void setPlayerLimit(int limit) {
        synchronized (players) {
            playerLimit = Math.max(0, limit);
            while (players.size() > playerLimit) {
                players.remove(players.size() - 1); // Removes the last player in the list
            }
        }
    }

    public int getPlayerLimit() {
        return playerLimit;
    }

    public Set<String> getPlayerNames() {
        synchronized (players) {
            return players.stream().map(Player::getName).collect(Collectors.toSet());
        }
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
