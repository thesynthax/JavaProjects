package neat;

import java.util.ArrayList;
import java.util.Comparator;

public class Specie
{
    private ArrayList<Player> players = new ArrayList<>();
    private double bestFitness = 0, averageFitness;

    private int staleness = 0;

    public Specie(Player p)
    {
        players.add(p);

        bestFitness = p.getFitness();
    }

    public void addToSpecie(Player p)
    {
        players.add(p);
    }

    public void sortPlayersInSpecie()
    {
        players.sort(
                new Comparator<Player>()
                {
                    @Override
                    public int compare(Player o1, Player o2)
                    {
                        return Double.compare(o1.getFitness(), o2.getFitness());
                    }
                }
        );

        if (players.size() == 0)
        {
            staleness = 200;
            return;
        }

        if (players.get(0).getFitness() > bestFitness)
        {
            staleness = 0;
            bestFitness = players.get(0).getFitness();
        }
        else
        {
            staleness++;
        }
    }

    public void setAverageFitness()
    {
        float sum = 0;
        for (Player p : players)
        {
            sum += p.getFitness();
        }
        averageFitness = sum/players.size();
    }

    public Player breed()
    {
        Player child;
        if (0.25f > Math.random())
            child = selectPlayer().clone();
        else
        {
            Player parent1 = selectPlayer();
            Player parent2 = selectPlayer();

            if (parent1.getFitness() < parent2.getFitness())
            {
                child =  parent2.crossOver(parent1);
            }
            else
            {
                child =  parent1.crossOver(parent2);
            }
        }

        child.getBrain().mutate();
        return child;
    }

    public Player selectPlayer()
    {
        double fitnessSum = 0;
        for (int i =0; i < players.size(); i++)
        {
            fitnessSum += players.get(i).getFitness();
        }

        double random = Math.random() * fitnessSum;
        double runningSum = 0;

        for (int i = 0; i < players.size(); i++)
        {
            runningSum += players.get(i).getFitness();
            if (runningSum > random)
            {
                return players.get(i);
            }
        }
        //unreachable code to make the parser happy
        return players.get(0);
    }

    public void cull()
    {
        if (players.size() > 2)
        {
            for (int i = players.size() / 2; i < players.size(); i++)
            {
                players.remove(i);
                i--;
            }
        }
    }
}
