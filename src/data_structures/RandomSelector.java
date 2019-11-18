package data_structures;

import java.util.ArrayList;

/*
* A Random object selector
 */
public class RandomSelector<T>
{
    private ArrayList<T> objects = new ArrayList<>();
    private ArrayList<Double> scores = new ArrayList<>();

    private double total_score = 0;

    //Objects with their scores are added
    public void add(T element, double score)
    {
        objects.add(element);
        scores.add(score);
        total_score+=score;
    }

    /*
     * Random selection, but based on the scores of the object
     * i.e. the objects with the higher score have a higher chance of being selected
     */
    public T random()
    {
        //Arbitrary names for the variables
        double v = Math.random() * total_score;

        double c = 0;
        for(int i = 0; i < objects.size(); i++)
        {
            c += scores.get(i);
            /*
             * The probability of 'c' being higher than 'v' is higher when the individual scores are higher
             * In simple words, 'c' will be higher when object scores will be higher
             */
            if(c > v)
            {
                return objects.get(i);
            }
        }
        return null;
    }

    //Used for resetting all the variable values
    public void reset()
    {
        objects.clear();
        scores.clear();
        total_score = 0;
    }

}
