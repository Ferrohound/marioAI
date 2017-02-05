package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;

import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.environments.MarioEnvironment;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Apr 8, 2009
 * Time: 4:03:46 AM
 */

public class ForwardAgent extends BasicMarioAIAgent implements Agent
{
int trueJumpCounter = 0;
int trueSpeedCounter = 0;
//private MarioEnvironment _environment;

public ForwardAgent()
{
    super("ForwardAgent");
    reset();
}

public void reset()
{
    action = new boolean[Environment.numberOfButtons];
    //action[Mario.KEY_RIGHT] = true;
    action[Mario.KEY_SPEED] = true;
    trueJumpCounter = 0;
    trueSpeedCounter = 0;
}

private boolean DangerOfGap(byte[][] levelScene)
{
	//receptiveFieldWidth
    int fromX = receptiveFieldWidth / 2;
    int fromY = receptiveFieldHeight / 2;
    
    //getReceptiveFieldCellValue(int x, int y) returns 0 if out of range
    if (fromX > 3)
    {
        fromX -= 2;
    }

    for (int x = fromX; x < receptiveFieldWidth; ++x)
    {
        boolean f = true;
        for (int y = fromY; y < receptiveFieldHeight; ++y)
        {
            if (getReceptiveFieldCellValue(y, x) != 0)
                f = false;
        }
        if (f ||
                getReceptiveFieldCellValue(marioCenter[0] + 1, marioCenter[1]) == 0 ||
                (marioState[1] > 0 &&
                        (getReceptiveFieldCellValue(marioCenter[0] + 1, marioCenter[1] - 1) != 0 ||
                                getReceptiveFieldCellValue(marioCenter[0] + 1, marioCenter[1]) != 0)))
            return true;
    }
    return false;
}


private boolean DangerOfGap()
{
    return DangerOfGap(levelScene);
}

//if there's an enemy within a certain range...
private boolean enemyNear(byte[][] levelScene){
	//byte[][] enemies = getEnemiesObservationZ(); 
	
	
	return false;
}

private boolean enemyNear(){
	return enemyNear(levelScene);
}



//byte[][] getLevelSceneObservationZ(level) from MarioEnvironment
//byte[][] getEnemiesObservationZ(level) from MarioEnvironment

/*
 *http://www.marioai.org/gameplay-track/marioai-benchmark 
 * 
 * 
 * 
 * 
 * (non-Javadoc)
 * @see ch.idsia.agents.controllers.BasicMarioAIAgent#getAction()
 */

public boolean[] getAction()
{
    // this Agent requires observation integrated in advance.

    if (getReceptiveFieldCellValue(marioCenter[0], marioCenter[1] + 2) != 0 ||
            getReceptiveFieldCellValue(marioCenter[0], marioCenter[1] + 1) != 0 ||
            DangerOfGap())
    {
        if (isMarioAbleToJump || (!isMarioOnGround && action[Mario.KEY_JUMP]))
        {
            action[Mario.KEY_JUMP] = true;
        }
        ++trueJumpCounter;
    } else
    {
        action[Mario.KEY_JUMP] = false;
        trueJumpCounter = 0;
    }

    if (trueJumpCounter > 16)
    {
        trueJumpCounter = 0;
        action[Mario.KEY_JUMP] = false;
    }
    
    //check if Mario is able to shoot
    if(isMarioAbleToShoot){
    	//shoot
    }

    //action[Mario.KEY_SPEED] = DangerOfGap();
    action[Mario.KEY_SPEED] =  !action[Mario.KEY_SPEED]; 
    
    action[Mario.KEY_RIGHT] = !(enemyNear());
    return action;
}
}
