package model;

import java.io.Serializable;
import java.util.Random;

/**
 * Classe ABSTRATA para todos Pokemons.
 * Contém atributos e métodos comuns a todos os tipos de Pokemon.
 * Implementa Serializable para permitir salvar o jogo.
 */
public abstract class Pokemon implements Serializable {
    
    // ----- ATRIBUTOS -----
    protected String name;
    protected PokemonType type;
    protected int level;
    protected int exp;
    protected int expToLevelUp;
    protected int health;
    protected int maxHealth;
    protected int attack;
    protected boolean isWild;
    protected boolean isParalyzed;

    // ----- CONSTRUTOR -----
    public Pokemon( String name, PokemonType type, int maxHealth, int attack) {
        this.name = name;
        this.type = type;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attack = attack;

        this.level = 5;
        this.exp = 0;                   // Devem ganhar em torno de 40 pontos por batalha.
        this.expToLevelUp = 100;
        this.isWild = true;
        this.isParalyzed = false;
    }


//  ================================================================================================
//  --- MÉTODOS ---
//  ================================================================================================

    //  --- COMBATE ----------------------------------------------------------------------------
    public abstract double getTypeAdvantage( Pokemon target );
    public abstract double getTypeAbility( Pokemon target );

    /**
     * Método de ataque comum a todos os Pokémons e que inicia a lógica de ataque.
     * @param target Pokemon alvo do ataque.
     * @return finalDamage = baseDamage * typeAdvantage * typeAbility;
     */
    public final void attack( Pokemon target ) {
        System.out.println( this.getName() + " ataca " + target.getName() + "!" );

        int baseDamage = this.getBaseDamage();
        double typeAdvantage = this.getTypeAdvantage( target );
        double typeAbility = this.getTypeAbility( target );

        int finalDamage = (int) (baseDamage * typeAdvantage * typeAbility);
        target.takeDamage( finalDamage );
    }

    protected int getBaseDamage() {
        return this.attack + new Random().nextInt( this. level + 1 );
    }

    public void takeDamage( int damage ) {
        this.health -= damage;
        if ( this.health < 0 ) {
            this.health = 0;
        }
    }

    //  --- NÍVEL E EXPERIÊNCIA ----------------------------------------------------------------
    public void increaseExpPoints ( int expPoints ) {
        this.exp += expPoints;
        if ( this.exp >= expToLevelUp ) {
            levelUp();
        }

        System.out.println( this.name + " ganhou " + expPoints + " pontos de experiência!" );
    }

    public void levelUp() {
        Random random = new Random();
        int variance = random.nextInt( 3 ) - 1; // Flutuação de -1 a 1.

        this.level++;
        this.expToLevelUp += this.level * this.level + this.level + variance;
        System.out.println( this.name + " subiu de nível!" );

        this.attack += ( this.attack + variance ) / 2;
        this.maxHealth += this.attack + this.level;
    }


// =================================================================================================
// ----- GETTERS E SETTERS -----
// =================================================================================================

    // ----- Getters -----
    public String getName()         { return name; }
    public PokemonType getType()    { return type; }

    public int getLevel()           { return level; }
    public int getExp()             { return exp; }
    public int getExpToLevelUp()    { return expToLevelUp; }
    public int getHealth()          { return health; }
    public int getMaxHealth()       { return maxHealth; }
    public int getAttack()          { return attack; }

    public boolean isWild()         { return isWild; }
    public boolean isParalyzed()    { return isParalyzed; }


    // ----- Setters -----
    public void setWild( boolean captured ) { this.isWild = captured; }
    public void setParalyzed( boolean paralyzed ) { this.isParalyzed = paralyzed; }
}
