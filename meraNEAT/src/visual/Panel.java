package visual;

import genome.ConnectionGene;
import genome.Genome;
import genome.NodeGene;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel
{

    private Genome genome;

    public Panel()
    {
    }

    public Genome getGenome()
    {
        return genome;
    }

    public void setGenome(Genome genome)
    {
        this.genome = genome;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        g.clearRect(0,0,10000,10000);
        g.setColor(Color.black);
        g.fillRect(0,0,10000,10000);

        for (ConnectionGene c:genome.getConnections())
        {
            paintConnection(c, (Graphics2D) g);
        }


        for (NodeGene n:genome.getNodes())
        {
            paintNode(n, (Graphics2D) g);
        }

    }

    private void paintNode(NodeGene n, Graphics2D g)
    {
        g.setColor(Color.gray);
        g.setStroke(new BasicStroke(3));
        g.drawOval((int)(this.getWidth() * n.getxPos()) - 10,
                (int)(this.getHeight() * n.getyPos()) - 10,20,20);
        g.drawString(n.toString(), (int)(this.getWidth() * n.getxPos()) - 4, (int)(this.getHeight() * n.getyPos()) + 4);
    }

    private void paintConnection(ConnectionGene c, Graphics2D g)
    {
        g.setColor(c.isEnabled() ? Color.green:new Color(73/255f, 82/255f, 76/255f, 0.5f));
        g.setStroke(c.isEnabled() ? new BasicStroke(3) : new BasicStroke(2));
        g.drawString(new String(c.getWeight() + "       ").substring(0,7),
                (int)((c.getOutNode().getxPos() + c.getInNode().getxPos())* 0.5 * this.getWidth()),
                (int)((c.getOutNode().getyPos() + c.getInNode().getyPos())* 0.5 * this.getHeight()) +15);
        g.drawLine(
                (int)(this.getWidth() * c.getInNode().getxPos()),
                (int)(this.getHeight() * c.getInNode().getyPos()),
                (int)(this.getWidth() * c.getOutNode().getxPos()),
                (int)(this.getHeight() * c.getOutNode().getyPos()));
    }

}
