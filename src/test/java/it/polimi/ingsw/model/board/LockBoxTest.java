package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exception.NotEnoughResourcesException;
import it.polimi.ingsw.model.game.Resource;
import junit.framework.TestCase;

import java.util.concurrent.locks.Lock;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class LockBoxTest extends TestCase {

    public void testSetCoin() {
        LockBox lBox = new LockBox();
        try{
            lBox.setAmountOf(Resource.COIN , 2);
            assertEquals(2 , lBox.getAmountOf(Resource.COIN));
            lBox.setAmountOf(Resource.COIN , -2);;
            assertEquals(0 , lBox.getAmountOf(Resource.COIN));
        }catch (NotEnoughResourcesException e){
            fail();
        }
        //fail the test if there isn't an exception
        try {
            lBox.setAmountOf(Resource.COIN , -5);;
            fail("Coin are:" + lBox.getAmountOf(Resource.COIN));
        }catch(NotEnoughResourcesException e){

        }
    }

    public void testSetServant() {
        LockBox lBox = new LockBox();
        try{
            lBox.setAmountOf(Resource.SERVANT , 2);
            assertEquals(2 , lBox.getAmountOf(Resource.SERVANT));
            lBox.setAmountOf(Resource.SERVANT , -2);
            assertEquals(0 , lBox.getAmountOf(Resource.SERVANT));
        }catch (NotEnoughResourcesException e){
            fail();
        }
        //fail the test if there isn't an exception
        try {
            lBox.setAmountOf(Resource.SERVANT , -5);
            fail("Servant are:" + lBox.getAmountOf(Resource.SERVANT));
        }catch(NotEnoughResourcesException e){

        }
    }

    public void testSetRock() {
        LockBox lBox = new LockBox();
        try{
            lBox.setAmountOf(Resource.ROCK , 2);
            assertEquals(2 , lBox.getAmountOf(Resource.ROCK));
            lBox.setAmountOf(Resource.ROCK , -2);
            assertEquals(0 , lBox.getAmountOf(Resource.ROCK));
        }catch (NotEnoughResourcesException e){
            fail();
        }
        //fail the test if there isn't an exception
        try {
            lBox.setAmountOf(Resource.ROCK , -5);
            fail("Rock are:" + lBox.getAmountOf(Resource.ROCK));
        }catch(NotEnoughResourcesException e){

        }
    }

    public void testSetShield() {
        LockBox lBox = new LockBox();
        try{
            lBox.setAmountOf(Resource.SHIELD , 2);
            assertEquals(2 , lBox.getAmountOf(Resource.SHIELD));
            lBox.setAmountOf(Resource.SHIELD , -2);
            assertEquals(0 , lBox.getAmountOf(Resource.SHIELD));
        }catch (NotEnoughResourcesException e){
            fail();
        }
        //fail the test if there isn't an exception
        try {
            lBox.setAmountOf(Resource.SHIELD , -5);
            fail("Shield are:" + lBox.getAmountOf(Resource.SHIELD));
        }catch(NotEnoughResourcesException e){

        }
    }

    public void testGetAmountOf(){
        LockBox lBox = new LockBox();
        try{
            lBox.setAmountOf(Resource.COIN , 5);
            assertEquals(5 , lBox.getAmountOf(Resource.COIN));
            lBox.setAmountOf(Resource.SHIELD , 2);
            assertEquals(2 , lBox.getAmountOf(Resource.SHIELD));
            lBox.setAmountOf(Resource.ROCK , 20);
            assertEquals(20 , lBox.getAmountOf(Resource.ROCK));
            lBox.setAmountOf(Resource.SERVANT , 3);
            assertEquals(3 , lBox.getAmountOf(Resource.SERVANT));

            lBox.setAmountOf(Resource.COIN , 5);
            assertEquals(10 , lBox.getAmountOf(Resource.COIN));
            lBox.setAmountOf(Resource.SHIELD , -1);
            assertEquals(1 , lBox.getAmountOf(Resource.SHIELD));
            lBox.setAmountOf(Resource.ROCK , -10);
            assertEquals(10 , lBox.getAmountOf(Resource.ROCK));
            lBox.setAmountOf(Resource.SERVANT , 17);
            assertEquals(20 , lBox.getAmountOf(Resource.SERVANT));

        }catch(NotEnoughResourcesException e){
            fail();
        }
    }

    public void testSetAmountOf(){
        LockBox lBox = new LockBox();
        try{
            lBox.setAmountOf(Resource.COIN , 5);
            assertEquals(5 , lBox.getAmountOf(Resource.COIN));
            lBox.setAmountOf(Resource.SHIELD , 2);
            assertEquals(2 , lBox.getAmountOf(Resource.SHIELD));
            lBox.setAmountOf(Resource.ROCK , 20);
            assertEquals(20 , lBox.getAmountOf(Resource.ROCK));
            lBox.setAmountOf(Resource.SERVANT , 3);
            assertEquals(3 , lBox.getAmountOf(Resource.SERVANT));

            lBox.setAmountOf(Resource.COIN , 5);
            assertEquals(10 , lBox.getAmountOf(Resource.COIN));
            lBox.setAmountOf(Resource.SHIELD , -1);
            assertEquals(1 , lBox.getAmountOf(Resource.SHIELD));
            lBox.setAmountOf(Resource.ROCK , -10);
            assertEquals(10 , lBox.getAmountOf(Resource.ROCK));
            lBox.setAmountOf(Resource.SERVANT , 17);
            assertEquals(20 , lBox.getAmountOf(Resource.SERVANT));

        }catch(NotEnoughResourcesException e){
            fail();
        }
        try {
            lBox.setAmountOf(Resource.COIN , -15);
            assertEquals(10 , lBox.getAmountOf(Resource.COIN));
            fail();
        }catch(NotEnoughResourcesException e){

        }
        try {
            lBox.setAmountOf(Resource.SHIELD , -100);
            assertEquals(1 , lBox.getAmountOf(Resource.SHIELD));
            fail();
        }catch(NotEnoughResourcesException e){

        }
        try {
            lBox.setAmountOf(Resource.ROCK , -15);
            assertEquals(10 , lBox.getAmountOf(Resource.ROCK));
            fail();
        }catch(NotEnoughResourcesException e){

        }
        try {
            lBox.setAmountOf(Resource.SERVANT , -25);
            assertEquals(20 , lBox.getAmountOf(Resource.SERVANT));
            fail();
        }catch(NotEnoughResourcesException e){

        }
    }

    public void testGetTotalAmountOfResources() {
        LockBox lBox = new LockBox();

        try{
            lBox.setAmountOf(Resource.COIN , 3);
            lBox.setAmountOf(Resource.SERVANT , 10);
            lBox.setAmountOf(Resource.SHIELD , 4);

            assertEquals(17 , lBox.getTotalAmountOfResources());
        }catch(Exception e){
            fail();
        }
    }
}






