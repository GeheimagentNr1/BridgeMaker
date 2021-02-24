package de.geheimagentnr1.bridge_maker.elements.datafixers;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.types.Type;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.TypeReferences;


public class ModDatafixers {
	
	
	private static final DataFixer DATA_FIXER = createFixer();
	
	private static DataFixer createFixer() {
		
		DataFixerBuilder datafixerbuilder = new DataFixerBuilder( SharedConstants.getVersion().getWorldVersion() );
		return datafixerbuilder.build( Util.getServerExecutor() );
	}
	
	public static Type<?> getType( String registryKey ) {
		
		return DATA_FIXER.getSchema( DataFixUtils.makeKey( SharedConstants.getVersion()
			.getWorldVersion() ) ).getChoiceType( TypeReferences.BLOCK_ENTITY, registryKey );
	}
}
