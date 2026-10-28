# Nílus Völgye – Android prototípus

Ez egy saját fejlesztésű, ókori Egyiptom ihlette városépítő prototípus. Nem tartalmazza a Children of the Nile eredeti kódját, grafikáit vagy hanganyagait.

## Mit tud jelenleg?
- Nílus-parti térképrajz
- élelem, arany és lakosság erőforrásai
- termőföld, ház, műhely és piramis építése
- évváltás, termelés és egyszerű népességi következmények
- érintéses kezelőfelület

## APK fordítása
1. Telepítsd az Android Studio aktuális stabil verzióját egy számítógépre.
2. Nyisd meg az ebben a ZIP-ben található `NileBuilderAndroid` mappát.
3. Várd meg a Gradle szinkronizálását és telepítsd az Android SDK 35-ös platformot, ha kéri.
4. Válaszd: **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
5. A létrejövő APK helye általában: `app/build/outputs/apk/debug/app-debug.apk`.

## Következő fejlesztési lépések
- mentés és betöltés
- nagyobb, görgethető térkép
- lakónegyedek és munkások
- több épület és célkitűzés
- hangok és saját készítésű grafika

A projekt forráskódot tartalmaz, nem előre lefordított APK-t. Ebben a munkakörnyezetben nincs telepített Android SDK/Gradle fordítókörnyezet, ezért itt nem tudtam ellenőrzött APK-fájlt előállítani.
