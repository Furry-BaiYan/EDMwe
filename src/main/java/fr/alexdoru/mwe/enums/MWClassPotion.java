package fr.alexdoru.mwe.enums;

import java.util.HashMap;
import java.util.Map;

public enum MWClassPotion {

    ANGEL(MWClass.ANGEL, 2, 16),
    ARCANIST(MWClass.ARCANIST, 2, 16),
    ASSASSIN(MWClass.ASSASSIN, 0, 0),
    AUTOMATON(MWClass.AUTOMATON, 3, 12),
    BLAZE(MWClass.BLAZE, 2, 16),
    COW(MWClass.COW, 1, 20),
    CREEPER(MWClass.CREEPER, 2, 16),
    DRAGON(MWClass.DRAGON, 2, 16),
    DREADLORD(MWClass.DREADLORD, 2, 16),
    ENDERMAN(MWClass.ENDERMAN, 2, 16),
    GOLEM(MWClass.GOLEM, 2, 16),
    HEROBRINE(MWClass.HEROBRINE, 2, 12),
    HUNTER(MWClass.HUNTER, 0, 0),
    MOLEMAN(MWClass.MOLEMAN, 2, 16),
    PHOENIX(MWClass.PHOENIX, 2, 16),
    PIGMAN(MWClass.PIGMAN, 1, 20),
    PIRATE(MWClass.PIRATE, 0, 0),
    RENEGADE(MWClass.RENEGADE, 2, 16),
    SHAMAN(MWClass.SHAMAN, 2, 16),
    SHARK(MWClass.SHARK, 2, 16),
    SHEEP(MWClass.SHEEP, 2, 16),
    SKELETON(MWClass.SKELETON, 2, 16),
    SNOWMAN(MWClass.SNOWMAN, 2, 16),
    SPIDER(MWClass.SPIDER, 2, 16),
    SQUID(MWClass.SQUID, 3, 12),
    WEREWOLF(MWClass.WEREWOLF, 1, 20),
    ZOMBIE(MWClass.ZOMBIE, 1, 16);

    public final MWClass mwClass;
    public final int potionCount;
    public final int hpPerPotion;

    private static final Map<MWClass, MWClassPotion> classMap = new HashMap<>();

    static {
        for (final MWClassPotion p : values()) {
            classMap.put(p.mwClass, p);
        }
    }

    MWClassPotion(MWClass mwClass, int potionCount, int hpPerPotion) {
        this.mwClass = mwClass;
        this.potionCount = potionCount;
        this.hpPerPotion = hpPerPotion;
    }

    public static MWClassPotion fromClass(MWClass mwClass) {
        return classMap.get(mwClass);
    }
}
