package neat;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * The population which is generated in each generation
 */
public class Population
{
    //Population of Individuals
    private ArrayList<Individual> population = new ArrayList<>();
    //Species
    private ArrayList<Specie> species = new ArrayList<>();

    //Constructor
    public Population(int size)
    {
        for (int i = 0; i < size; i++)
        {
            population.add(new Individual());
            population.get(i).getBrain().setNeat(new NEAT());
            population.get(i).getBrain().mutate();
        }
    }

    private void updateAliveIndividuals()
    {
        for (Individual i : population)
        {
            if (!i.isDead())
            {
                i.look();
                i.think();
                i.update();
            }
        }
    }

    //Evolving and incrementing the generation
    private void evolve()
    {
    	//genSpecies
    	//Calculate Fitness
    	for (Individual i : population)
    	{
    		i.calculateFitness();
    	}

    	for (Specie s : species)
    	{
    		s.sortIndividualsInSpecie();
    	}

    	sortSpecies();
    }

    //Generating the species and sorting the players inside each species
    private void generateSpecies()
    {
        for (Specie s : species)
        {
            s.getIndividuals().clear();
        }
    }

    //Kill the species whose fitness hasn't increased over several generations
    private void killStaleSpecies()
    {
    	for (int i = 2; i< species.size(); i++) 
    	{
      		if (species.get(i).getStaleness() >= 15)
      		{
		        species.remove(i);
		        i--;
      		}
    	}
    }

    //The weak species will be killed
    private void killBadSpecies()
    {
    	float avgSum = avgFitness() * population.size();

    	for (int i = 0; i < species.size(); i++)
    	{
    		if (species.get(i).getAverageFitness()/avgSum * population.size() < 1)
    		{
    			species.remove(i);
    			i--;
    		}
    	}
    }

    //Slaughter the bottom half or the whole specie except the best
    private void cull(boolean killAllExceptBest)
    {
        for (Specie s : species)
        {
            s.cull(false);
            s.setAverageFitness();
        }
    }

    //Sort each specie based on their best performer
    private void sortSpecies()
    {
        species.sort(
            new Comparator<Specie>()
            {
                @Override
                public int compare(Specie o1, Specie o2)
                {
                    return Double.compare(o1.getBestFitness(), o2.getBestFitness());
                }
            }
        );
    }

    //Get average fitness
    private float avgFitness()
    {
        float sum = 0;
        for (Specie s : species)
        {
            sum += s.getAverageFitness();
        }
        return sum/species.size();
    }

    public boolean allDead()
    {
        for (Individual i : population)
        {
            if (!i.isDead())
                return false;
        }
        return true;
    }
}
