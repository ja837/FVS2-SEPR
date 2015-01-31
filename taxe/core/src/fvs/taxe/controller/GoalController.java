package fvs.taxe.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fvs.taxe.TaxeGame;
import gameLogic.GameState;
import gameLogic.GameStateListener;
import gameLogic.Player;
import gameLogic.PlayerManager;
import gameLogic.goal.Goal;

import java.util.ArrayList;
import java.util.List;

public class GoalController {
	public final static int CONTROLS_WIDTH = 250;
    private Context context;
    private TextButton endTurnButton;
    private Color controlsColor = Color.LIGHT_GRAY;
    private Group goalButtons = new Group();

    public GoalController(Context context) {
        this.context = context;
    }

    private List<String> playerGoalStrings() {
        ArrayList<String> strings = new ArrayList<String>();
        PlayerManager pm = context.getGameLogic().getPlayerManager();
        Player currentPlayer = pm.getCurrentPlayer();

        for (Goal goal : currentPlayer.getGoals()) {
            if(goal.getComplete()) {
                continue;
            }

            strings.add(goal.toString());
        }

        return strings;
    }
    
    public void addEndTurnButton() {
        endTurnButton = new TextButton("End Turn", context.getSkin());
        endTurnButton.setPosition(TaxeGame.WIDTH - CONTROLS_WIDTH + 25, TaxeGame.HEIGHT - 623.0f);
        endTurnButton.setHeight(50);
        endTurnButton.setWidth(100);
        endTurnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                context.getGameLogic().getPlayerManager().turnOver();
            }
        });

        context.getGameLogic().subscribeStateChanged(new GameStateListener() {
            @Override
            public void changed(GameState state) {
                if(state == GameState.NORMAL) {
                    endTurnButton.setVisible(true);
                } else {
                    endTurnButton.setVisible(false);
                }
            }
        });

        context.getStage().addActor(endTurnButton);
    }
    
    public void drawBackground() {
        TaxeGame game = context.getTaxeGame();

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(controlsColor);
        game.shapeRenderer.rect(TaxeGame.WIDTH - CONTROLS_WIDTH, 0, CONTROLS_WIDTH, TaxeGame.HEIGHT);
        game.shapeRenderer.setColor(Color.BLACK);
        game.shapeRenderer.rect(TaxeGame.WIDTH - CONTROLS_WIDTH, 0, 1, TaxeGame.HEIGHT );
        game.shapeRenderer.end();
    }

    public void showCurrentPlayerGoals() {
        TaxeGame game = context.getTaxeGame();
        
        goalButtons.remove();
        goalButtons.clear();

        float top = (float) TaxeGame.HEIGHT;
        float x = TaxeGame.WIDTH - CONTROLS_WIDTH + 25;
        float y = top - 10.0f;

        game.batch.begin();
        game.fontSmall.setColor(Color.NAVY);
        game.fontSmall.draw(game.batch, playerGoalHeader(), x, y);
        game.batch.end();
        
        y -= 25;

        for (String goalString : playerGoalStrings()) {
            
            game.batch.begin();
            game.fontSmall.setColor(Color.BLACK);
            game.fontSmall.drawWrapped(game.batch, goalString, x, y, CONTROLS_WIDTH - 50);
            game.batch.end();

            y -= 70;
        }
        
        context.getStage().addActor(goalButtons);
    }

    private String playerGoalHeader() {
        return "Player " + context.getGameLogic().getPlayerManager().getCurrentPlayer().getPlayerNumber() + " Goals:";
    }
}
