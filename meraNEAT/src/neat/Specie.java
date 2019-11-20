package neat;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * A specie is a part of population in which compatible individuals are placed
 */
public class Specie
{
    //List of individuals in this specie
    private ArrayList<Individual> individuals = new ArrayList<>();
    //Fittest fitness in the specie
    private double bestFitness = 0, averageFitness;

    //Number of generations gone without improvement in the best fitness
    private int staleness = 0;

    //Constructor
    public Specie(Individual p)
    {
        individuals.add(p);

        bestFitness = p.getFitness();
    }

    //Function to add individual to this specie
    public void addToSpecie(Individual p)
    {
        individuals.add(p);
    }

    //Sorting Individuals based upon their fitness
    public void sortIndividualsInSpecie()
    {
        individuals.sort(
                new Comparator<Individual>()
                {
                    @Override
                    public int compare(Individual o1, Individual o2)
                    {
                        return Double.compare(o1.getFitness(), o2.getFitness());
                    }
                }
        );

        if (individuals.size() == 0)
        {
            staleness = 200;
            return;
        }

        if (individuals.get(0).getFitness() > bestFitness)
        {
            staleness = 0;
            bestFitness = individuals.get(0).getFitness();
        }
        else
        {
            staleness++;
        }
    }

    //Average Fitness set
    public void setAverageFitness()
    {
        float sum = 0;
        for (Individual p : individuals)
        {
            sum += p.getFitness();
        }
        averageFitness = sum/ individuals.size();
    }

    //Breeding two individuals in this specie
    public Individual breed()
    {
        Individual child;
        if (0.25f > Math.random())
            child = selectIndividual().clone();
        else
        {
            Individual parent1 = selectIndividual();
            Individual parent2 = selectIndividual();

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

    //Selecting a random individual based upon their fitness i.e. the fitter individual will have higher chance of getting selected
    public Individual selectIndividual()
    {
        double fitnessSum = 0;
        for (int i = 0; i < individuals.size(); i++)
        {
            fitnessSum += individuals.get(i).getFitness();
        }

        double random = Math.random() * fitnessSum;
        double runningSum = 0;

        for (int i = 0; i < individuals.size(); i++)
        {
            runningSum += individuals.get(i).getFitness();
            if (runningSum > random)
            {
                return individuals.get(i);
            }
        }
        //unreachable code to make the parser happy
        return individuals.get(0);
    }

    public void cull()
    {
        if (individuals.size() > 2)
        {
            for (int i = individuals.size() / 2; i < individuals.size(); i++)
            {
                individuals.remove(i);
                i--;
            }
        }
    }
}
