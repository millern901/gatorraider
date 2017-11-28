package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.Defender;
import game.models.Attacker;
import game.models.Node;
import game.models.Game;

import java.util.List;

public final class StudentController implements DefenderController
{
	public void init(Game game)
    {
    }

	public void shutdown(Game game) { }

	public int[] update(Game game,long timeDue)
	{
		int[] actions = new int[Game.NUM_DEFENDER];
		List<Defender> enemies = game.getDefenders();
		Attacker paku = game.getAttacker();
		//Chooses a random LEGAL action if required. Could be much simpler by simply returning
        //any random number of all of the ghosts
        for(int i = 0; i < actions.length; i++)
		{
            Defender defender = enemies.get(i);
            actions[i] = dumbChase(defender,paku);
        }
//        print_moves(actions);
        return actions;
	}

    private void print_moves(int [] actions)
    {
        for(int i = 0; i < actions.length; i++){
            System.out.print(i + " ");
            switch (actions[i])
            {
                case 2:
                    System.out.print("UP ");
                    break;
                case 3:
                    System.out.print("RIGHT ");
                    break;
                case 0:
                    System.out.print("DOWN ");
                    break;
                case 1:
                    System.out.print("LEFT ");
                    break;
                default:
                    System.out.print("NIL ");
                    break;
            }
        }
        System.out.println();
    }

	private int dumbChase(Defender ghost, Attacker paku)
    {
        return ghost.getNextDir(paku.getLocation(), !ghost.isVulnerable());
    }
}