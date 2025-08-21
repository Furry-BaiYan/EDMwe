//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Deobfuscator3000\mappings"!

//Decompiled by Procyon!

package fr.alexdoru.mwe.mark;

import net.minecraft.util.*;
import java.sql.*;
import java.awt.*;
import java.util.*;

public class Marker
{
    private Symbol symbol;
    private BlockPos blockPos;
    private Timestamp timestamp;
    private static EnumMap<Symbol, String> encodeMap;
    private static final Map<String, Symbol> decodeMap;
    private static EnumMap<Symbol, Color> colorMap;
    
    private static void putMaps(final Symbol symbol, final String s, final Color c) {
        Marker.encodeMap.put(symbol, s);
        Marker.decodeMap.put(s, symbol);
        Marker.colorMap.put(symbol, c);
    }
    
    public Marker(final BlockPos blockPos, final Symbol symbol) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.blockPos = blockPos;
        this.symbol = symbol;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
    
    public static Marker fromData(final int x, final int y, final int z, final String symbolCode) {
        return new Marker(new BlockPos(x, y, z), Marker.decodeMap.get(symbolCode));
    }
    
    public BlockPos getBlockPos() {
        return this.blockPos;
    }
    
    public static String getCode(final Symbol symbol) {
        return Marker.encodeMap.get(symbol);
    }
    
    public String getCode() {
        return getCode(this.symbol);
    }
    
    public static String getCode(final int index) {
        return getCode(Symbol.values()[index]);
    }
    
    public Color getColor() {
        return Marker.colorMap.get(this.symbol);
    }
    
    public Symbol getSymbol() {
        return this.symbol;
    }
    
    public int getTextureIndex() {
        return this.symbol.ordinal();
    }
    
    public Timestamp getTimestamp() {
        return this.timestamp;
    }
    
    static {
        Marker.encodeMap = new EnumMap<Symbol, String>(Symbol.class);
        decodeMap = new HashMap<String, Symbol>();
        Marker.colorMap = new EnumMap<Symbol, Color>(Symbol.class);
        putMaps(Symbol.HERE, "x", new Color(249, 255, 254));
        putMaps(Symbol.NOTICE, "n", new Color(254, 216, 61));
        putMaps(Symbol.QUESTION, "q", new Color(249, 128, 29));
        putMaps(Symbol.NO, "N", new Color(157, 157, 151));
        putMaps(Symbol.YES, "Y", new Color(128, 199, 31));
        putMaps(Symbol.DEFEND, "d", new Color(60, 68, 170));
        putMaps(Symbol.ATTACK, "a", new Color(176, 46, 38));
        putMaps(Symbol.MINE, "m", new Color(199, 78, 189));
    }
    
    public enum Symbol
    {
        HERE, 
        NOTICE, 
        QUESTION, 
        NO, 
        YES, 
        DEFEND, 
        ATTACK, 
        MINE;
        
        private static /* synthetic */ Symbol[] $values() {
            return new Symbol[] { Symbol.HERE, Symbol.NOTICE, Symbol.QUESTION, Symbol.NO, Symbol.YES, Symbol.DEFEND, Symbol.ATTACK, Symbol.MINE };
        }
    }
}
