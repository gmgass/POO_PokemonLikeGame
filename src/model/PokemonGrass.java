package model;

public class PokemonGrass extends Pokemon {

    // ----- CONSTRUTOR -----
    public PokemonGrass( String name, int maxHealth, int attack ) {
        super( name, PokemonType.GRASS, maxHealth, attack );
    }

    @Override
    public double getTypeAdvantage( Pokemon target ) {
        PokemonType targetType = target.getType();

        return switch ( targetType ) {
            case ELECTRIC -> 1.0;
            case GRASS -> 1.0;
            case GROUND -> 1.5;
            case WATER -> 1.5;
            default -> 1.0;
        };
    }

    @Override
    public double getTypeAbility() {
        return 1.0;
    }
}


