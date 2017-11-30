package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.*;

import java.util.List;

    public final class StudentController implements DefenderController
    {
        public void init(Game game) {
        }

        public void shutdown(Game game) {
        }

        public int[] update(Game game, long timeDue) {
            int[] actions = new int[Game.NUM_DEFENDER];

                actions[0] = superTrapperUpdate(game.getAttacker(), game.getDefender(0), game, game.getCurMaze());
                actions[1] = trapperUpdate(game.getAttacker(), game.getDefender(1), game.getCurMaze(),game);
                actions[2] = pincherUpdate(game.getAttacker(), game.getDefender(2), game.getCurMaze(),game);
                actions[3] = dumbChaserUpdate(game.getAttacker(), game.getDefender(3), game.getCurMaze(),game);

            return actions;
        }

        // ** Ghost AI methods **

        private int trapperUpdate(Attacker attacker, Defender defender,Maze m,Game game) {
            if(PakuOnPowerPill(attacker,m)){
                return run(defender,m,attacker,game,3);
            }
            Node target = attacker.getLocation().getNeighbor(attacker.getDirection());
            if (target == null)
                return defender.getNextDir(attacker.getLocation(), !defender.isVulnerable());
            else
                return defender.getNextDir(target, !defender.isVulnerable());
        }

        private int dumbChaserUpdate(Attacker paku, Defender ghost,Maze m,Game game) {
            if(PakuOnPowerPill(paku,m)){
                return run(ghost,m,paku,game,1);
            }
            return ghost.getNextDir(paku.getLocation(), !ghost.isVulnerable());
        }

        private int pincherUpdate(Attacker attacker, Defender defender,Maze m,Game game) {
            if(PakuOnPowerPill(attacker,m)){
                return run(defender,m,attacker,game,2);
            }
            Node target = attacker.getLocation().getNeighbor(attacker.getReverse());
            if (target == null)
                return defender.getNextDir(attacker.getLocation(), !defender.isVulnerable());
            else
                return defender.getNextDir(target, !defender.isVulnerable());
        }

        private int superTrapperUpdate(Attacker attacker, Defender defender, Game game , Maze m) {
            if(PakuOnPowerPill(attacker,m)){
                return run(defender,m,attacker,game,0);
            }
            Node target = attacker.getLocation().getNeighbor(attacker.getDirection());

            if (target == null)
                defender.getNextDir(attacker.getLocation(), !defender.isVulnerable());

            else {
                Node superTarget = target.getNeighbor(attacker.getDirection());

                if (!(superTarget == null) && defender.getLocation().getPathDistance(superTarget) > 3) {
                    return defender.getNextDir(superTarget, !defender.isVulnerable());
                } else return defender.getNextDir(attacker.getLocation(), !defender.isVulnerable());
            }
            return -1;
        }

        private boolean PakuOnPowerPill(Attacker Paku,Maze m)
        {
            for (Node n : m.getPowerPillNodes()) {
                if(Paku.getLocation().getPathDistance(n) < 5) {
                    return true;
                }
            }
            return false;
        }

        private int run(Defender ghost,Maze m,Attacker Paku,Game game,int defenderNumber){

            int distance = Integer.MIN_VALUE;
            Node fatherest = null;
            Node[] otherpill = new Node[3];
            int i = 0;
            for (Node n : m.getPowerPillNodes()){
                if(n.getPathDistance(Paku.getLocation()) > 5) {
                    otherpill[i] = n;
                    i++;
                }
            }

            if(defenderNumber == 3 && game.getDefender(0).getLocation().getPathDistance(otherpill[0]) < 40 && game.getDefender(3).getLocation().getPathDistance(otherpill[1]) < 40 && game.getDefender(2).getLocation().getPathDistance(otherpill[2]) <  40){
                return ghost.getNextDir(Paku.getLocation(),!ghost.isVulnerable());
            }
            if(defenderNumber != 3) {
                return ghost.getNextDir(otherpill[defenderNumber], true);
            }else{
                return ghost.getNextDir(otherpill[0], true);
            }
        }
        // ** Helper methods **

        /**
         * Simple debugging method to confirm that ghosts are moving as intended.
         * Prints the ghost's ID and the current direction out update method is feeding to the ghost in a single line.
         *
         * @param actions array of ghost moves
         */
        private void print_moves(int[] actions) {
            for (int i = 0; i < actions.length; i++) {
                System.out.print(i + " ");
                switch (actions[i]) {
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



        /**
         * Blanchard's default code
         * @param game game state
         * @return actions array
         */
        private int[] blanchard(Game game) {
            int[] actions = new int[4];
            List<Defender> enemies = game.getDefenders();
            for (int i = 0; i < actions.length; i++) {
                Defender defender = enemies.get(i);
                List<Integer> possibleDirs = defender.getPossibleDirs();
                if (possibleDirs.size() != 0)
                    actions[i] = possibleDirs.get(Game.rng.nextInt(possibleDirs.size()));
                else
                    actions[i] = -1;
            }
            return actions;
        }
}