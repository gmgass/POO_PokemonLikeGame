package model;

public class PokemonGround extends Pokemon {

    // ----- CONSTRUTOR -----
    public PokemonGround( String name, int maxHealth, int attack ) {
        super( name, PokemonType.GROUND, maxHealth, attack );
    }

    @Override
    public double getTypeAdvantage( Pokemon target ) {
        PokemonType targetType = target.getType();

        return switch ( targetType ) {
            case ELECTRIC -> 1.0;
            case GRASS -> 1.0;
            case GROUND -> 1.0;
            case WATER -> 0.5;
            default -> 1.0;
        };
    }

    @Override
    public double getAbilityDamage( Pokemon target ) {
        return 1.0;
    }
}


