package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.Defender;
import game.models.Game;
import game.models.Node;
import game.models.Attacker;

import java.util.List;

public final class StudentController implements DefenderController
{
	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int[] update(Game game,long timeDue)
	{
        int[] actions = new int[Game.NUM_DEFENDER];
        List<Defender> enemies = game.getDefenders();

        for(int i = 0; i < actions.length; i++) {
            Defender defender = enemies.get(i);

            if (i == 0 || i == 1) {
                int direction = game.getAttacker().getDirection();
                Node target = game.getAttacker().getLocation().getNeighbor(direction);
                if (target == null)
                    actions[i] = defender.getNextDir(game.getAttacker().getLocation(), !defender.isVulnerable());
                else
                    actions[i] = defender.getNextDir(target, !defender.isVulnerable());
            }

            if (i == 2 || i == 3) {
                actions[i] = defender.getNextDir(game.getAttacker().getLocation(), !defender.isVulnerable());
            }

        }
        return actions;
	}


    private int dumbChaser(Attacker attacker, Defender defender)
    {
            return defender.getNextDir(attacker.getLocation(), !defender.isVulnerable());

	}

    private int trapper(Attacker attacker, Defender defender)
    {
        Node target = attacker.getLocation().getNeighbor(attacker.getDirection());
        if (target == null)
            return defender.getNextDir(attacker.getLocation(), !defender.isVulnerable());
        else
            return defender.getNextDir(target, !defender.isVulnerable());
    }






	}