package model;

import java.util.Random;

public class PokemonGround extends Pokemon {

    private int turnCounter = 1;    // Pode náo. DEVE ser static

    // ----- CONSTRUTOR -----
    public PokemonGround( String name, int maxHealth, int attack ) {
        super( name, PokemonType.GROUND, maxHealth, attack );
    }

    @Override
    public double getTypeAdvantage( Pokemon target ) {
        PokemonType targetType = target.getType();

        return switch ( targetType ) {
            case WATER -> 0.5;
            default -> 1.0; // ELECTRIC, GRASS, GROUND.
        };
    }

    @Override
    public String toString() {
        return this.name + " Tipo: Terra";
    }

    // -----------------------------------------------------------------------------------------
    // HABILIDADE: DANO DOBRADO
    // Causa 200% de dano em turno ímpar.
    // 20% de chance de causar 150% de dano em turno par.
    // -----------------------------------------------------------------------------------------
    @Override
    public double getAbilityDamage( Pokemon target ) {
        
        if ( turnCounter % 2 != 0 ) {
            return 2.0;
        } else {
            Random random = new Random();
            if ( random.nextInt( 5 ) == 0 ) {
                return 1.5;
            }
        }
        turnCounter++;

        return 1.0;
    }

    @Override
    public void applyAbilityEffect( Pokemon target, int damage ) {}
}


