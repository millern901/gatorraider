package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.*;

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

            if (i == 0) {
                actions[i] = superTrapper(game.getAttacker(), game.getDefender(i), game);
            }
            if (i == 1) {
                actions[i] = trapper(game.getAttacker(), game.getDefender(i));
            }
            if (i == 2) {
                actions[i] = pincher(game.getAttacker(), game.getDefender(i));
            }
            if (i == 3) {
                actions[i] = dumbChaser(game.getAttacker(), game.getDefender(i));
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

    private int pincher(Attacker attacker, Defender defender)
    {
        Node target = attacker.getLocation().getNeighbor(attacker.getReverse());
        if (target == null)
            return defender.getNextDir(attacker.getLocation(), !defender.isVulnerable());
        else
            return defender.getNextDir(target, !defender.isVulnerable());
    }

    private int superTrapper(Attacker attacker, Defender defender, Game game) {
        Node target = attacker.getLocation().getNeighbor(attacker.getDirection());

        if (target == null)
            defender.getNextDir(attacker.getLocation(), !defender.isVulnerable());

        else {
            Node superTarget = target.getNeighbor(attacker.getDirection());

            if (!(superTarget == null) && defender.getLocation().getPathDistance(superTarget) > 3) {
                return defender.getNextDir(superTarget, !defender.isVulnerable());
            }
            else return defender.getNextDir(attacker.getLocation(), !defender.isVulnerable());
        }
        return -1;
    }


	}