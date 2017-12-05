package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.*;

import java.util.List;

    public final class StudentController implements DefenderController
    {
        public void init(Game game) {}

        public void shutdown(Game game) {}

        public int[] update(Game game, long timeDue) {
            int[] actions = new int[Game.NUM_DEFENDER];

                actions[0] = cutOffUpdate(game.getAttacker(), game.getDefender(0), game);
                actions[1] = baitUpdate(game.getAttacker(), game.getDefender(1));
                actions[2] = pincherUpdate(game.getAttacker(), game.getDefender(2), game);
                actions[3] = dumbChaserUpdate(game.getAttacker(), game.getDefender(3), game);

            return actions;
        }

        // ** Ghost AI methods **

        /**
         * Simple cut off method gets the path to the position in front of paku.
         * @version 1.0
         * @author Nicholas Miller <https://github.com/millern901/gatorraider>
         *
         * @param paku attacker object
         * @param ghost defender object
         * @param game game state
         * @return direction of movement
         */
        private int cutOffUpdate(Attacker paku, Defender ghost, Game game) {
            if(PakuOnPowerPill(paku, game.getCurMaze())) {
                return run(ghost, paku, game, 2);
            }
            Node target = targetInFront(paku);
            return PakuBlocksPathFinder(paku, ghost, target);
        }

        /**
         *Simple bait method that attacks paku if he is a certain distance away from a powerPill.
         * @version 1.0
         * @author -----Jack----- <https://github.com/-----GitHubNameHere-----/gatorraider>
         *
         * @param paku attacker object
         * @param ghost defender object
         * @return direction of movement
         */
        private int baitUpdate(Attacker paku, Defender ghost) {
            Node target = paku.getLocation().getNeighbor(paku.getDirection());
            if (target == null)
                return ghost.getNextDir(paku.getLocation(), !ghost.isVulnerable());
            else
                return ghost.getNextDir(target, !ghost.isVulnerable());
        }

        /**
         * Simple pinch method gets the path to the position behind paku.
         * @version 1.0
         * @author -----Carl----- <https://github.com/-----GitHubNameHere-----/gatorraider>
         *
         * @param paku attacker object
         * @param ghost defender object
         * @param game game state
         * @return direction of movement
         */
        private int pincherUpdate(Attacker paku, Defender ghost,Game game) {
            if(PakuOnPowerPill(paku, game.getCurMaze())){
                return run(ghost, paku, game,2);
            }
            Node target = targetBehind(paku);
            return PakuBlocksPathFinder(paku, ghost, target);
        }

        /**
         * Simple chaser method gets the path to the position of paku.
         * @version 1.0
         * @author Kevin Nguyen <https://github.com/ngynkvn/gatorraider>
         *
         * @param paku attacker object
         * @param ghost defender object
         * @param game game state
         * @return direction of movement
         */
        private int dumbChaserUpdate(Attacker paku, Defender ghost, Game game) {
            if(PakuOnPowerPill(paku, game.getCurMaze())){
                return run(ghost, paku, game,1);
            }
            return ghost.getNextDir(paku.getLocation(), !ghost.isVulnerable());
        }

        // **Methods used within AI methods**

        /**
         * Simple method to find the path to paku if paku lies on the shortest path to him
         *
         * @param paku
         * @param ghost
         * @param target node
         * @return is paku on path to node
         */
        private int PakuBlocksPathFinder(Attacker paku, Defender ghost, Node target) {
            List<Integer> neighbors = ghost.getPossibleDirs();
            int direction = ghost.getDirection();

            if (isPakuOnPath(paku, ghost, target)) {
                for (int x = 0; x < neighbors.size(); x++)
                    if (neighbors.get(x) ==  direction) neighbors.remove(x);
                return compareDistances(neighbors, ghost, target);
            }

            return ghost.getNextDir(target, !ghost.isVulnerable());
        }

        /**
         * simple method to get a new direction of movement if paku lies on the path of movement
         *
         * @param neighbors
         * @param ghost
         * @param target
         * @return direction of movement
         */
        private int compareDistances(List<Integer> neighbors, Defender ghost, Node target) {
            int direction = 0;
            int tempDistance = 0;
            for (int y = 0; y < neighbors.size(); y++) {
                if (neighbors.get(y) == null)
                    continue;
                else if (tempDistance > distanceToNode(ghost.getLocation().getNeighbor(neighbors.get(y)), target)) {
                    direction = y;
                    tempDistance = distanceToNode(ghost.getLocation().getNeighbor(neighbors.get(y)), target);
                }
            }
            return direction;
        }

        /**
         * simple method to calculate the distance to a Node from a Node in a line
         *
         * @param position
         * @param target
         * @return int distance
         */
        private int distanceToNode(Node position, Node target) {
            return (int)Math.sqrt(Math.pow(target.getX() - position.getX(), 2) + Math.pow(target.getY() - position.getY(), 2));
        }

        /**
         * Simple method to determine if paku is on the path
         *
         * @param paku
         * @param ghost
         * @param target
         * @return is paku on path
         */
        private boolean isPakuOnPath(Attacker paku, Defender ghost, Node target) {
            List<Node> listToPaku = ghost.getPathTo(target);

            for (Node node: listToPaku) {
                if (paku.getLocation().equals(node) ) return true;
            }
            return false;
        }

        /**
         * Simple method to determine the Node in front of paku
         *
         * @param paku
         * @return Node in front of paku
         */
        private Node targetInFront(Attacker paku) {
            Node target = paku.getLocation().getNeighbor(paku.getDirection());
            if (target == null)
                return paku.getLocation();
            else {
                Node superTarget = target.getNeighbor(paku.getDirection());
                if (superTarget == null) {
                    return target;
                }
                return superTarget;
            }
        }

        /**
         * Simple method to determine the Node behind paku
         *
         * @param paku
         * @return Node behind paku
         */
        private Node targetBehind(Attacker paku) {
            Node target = paku.getLocation().getNeighbor(paku.getReverse());
            if (target == null)
                return paku.getLocation();
            else {
                Node superTarget = target.getNeighbor(paku.getReverse());
                if (superTarget == null) {
                    return target;
                }
                return superTarget;
            }
        }

        /**
         * Simple method to check if paku is near a powerpill
         *
         * @param Paku
         * @param maze
         * @return if paku is near a powerpill
         */
        private boolean PakuOnPowerPill(Attacker Paku, Maze maze) {
            for (Node n : maze.getPowerPillNodes()) {
                if(Paku.getLocation().getPathDistance(n) < 5) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Simple method that returns the direction of the corner for the ghost to run in
         *
         * @param ghost
         * @param Paku
         * @param game state
         * @param defenderNumber
         * @return direction of movement
         */
        private int run(Defender ghost, Attacker Paku, Game game, int defenderNumber){
            Node[] otherpill = new Node[3];
            int i = 0;
            for (Node n : game.getCurMaze().getPowerPillNodes()){
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