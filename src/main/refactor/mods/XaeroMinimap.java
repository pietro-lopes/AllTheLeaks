//package github.uncandango.alltheleaks.mods;
//
//import github.uncandango.alltheleaks.mixin.MinimapInterfaceExtension;
//import xaero.common.XaeroMinimapSession;
//import xaero.common.minimap.radar.MinimapRadarList;
//
//import java.util.Iterator;
//
//public interface XaeroMinimap {
//    static void run () {
//        var minimapRadar = XaeroMinimapSession.getCurrentSession().getMinimapProcessor().getEntityRadar();
//        for (Iterator<MinimapRadarList> it = minimapRadar.getRadarListsIterator(); it.hasNext(); ) {
//            var list = it.next();
//            var entities = list.getEntities();
//            entities.clear();
//        }
//    }
//}
