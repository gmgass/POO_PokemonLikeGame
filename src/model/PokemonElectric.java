package model;

import java.util.Random;

public class PokemonElectric extends Pokemon {

    // ----- CONSTRUTOR -----
    public PokemonElectric( String name, int maxHealth, int attack ) {
        super( name, PokemonType.ELECTRIC, maxHealth, attack );
    }

    @Override
    public double getTypeAdvantage( Pokemon target ) {
        PokemonType targetType = target.getType();

        return switch ( targetType ) {
            case GROUND -> 0.0;
            case WATER -> 2.0;
            default -> 1.0; // ELECTRIC, WATER.
        };
    }

    @Override
    public String toString() {
        return this.name + " Tipo: Elétrico";
    }

    // -----------------------------------------------------------------------------------------
    // HABILIDADE: PARALISIA
    // 25% de chance de causar paralisia contra pokémons do tipo ELÉTRICO e TERRA.
    // 33% de chance de causar paralisia contra pokémons do tipo GRAMA.
    // 50% de chance de causar paralisia contra pokémons do tipo ÁGUA.
    // -----------------------------------------------------------------------------------------
    @Override
    public double getAbilityDamage( Pokemon target ) {
        return 1.0;
    }

    @Override
    public void applyAbilityEffect( Pokemon target, int damage ) {
        PokemonType targetType = target.getType();
        Random random = new Random();
        
        switch ( targetType ) {
            case ELECTRIC, GROUND:  if ( random.nextInt( 4 ) == 0 ) { target.setParalyzed( true ); } break;
            case GRASS:             if ( random.nextInt( 3 ) == 0 ) { target.setParalyzed( true ); } break;
            case WATER:             if ( random.nextInt( 2 ) == 0 ) { target.setParalyzed( true ); } break;
        }
    }
}