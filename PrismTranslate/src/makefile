
# javac --add-modules java.se.ee => to access javax.xml.bind !!!
# [ https://blog.codefx.org/java/java-9-migration-guide/#Dependencies-On-Java-EE-Modules ]
#

# ABCL_ROOT=${HOME}/lib/abcl
ABCL_ROOT=/opt/local/share/java/abcl
ABCL_ROOT=../
ABCL_ROOT=/home/christos/lib/abcl
ABCL_ROOT := $(shell ./where-is-abcl)
BIN=../bin
CMS=../Cms
ORG=../xjc
ORGBIN=$(BIN)
ORGBIN=$(ORG)
FBIN=../fbin
# JFLAGS=-disableassertions -cp $(BIN)//:
JFLAGS=-enableassertions
JFLAGS=-enableassertions --add-modules java.se.ee

all: $(BIN)/cumulus-xsd-schema.jar $(BIN)/translator.jar

$(BIN)/cumulus-xsd-schema.jar: CMMonitoring.xsd LifeCycleModel.xsd NewCommonPartsCM.xsd simpleMode.xml makefile
	-rm -rf $(ORG)/
	-mkdir $(ORG)/
	@#xjc -d $(ORG) CMMonitoring.xsd -extension simpleMode.xml
	xjc -d $(ORG)  ws-agreement-demo.xsd -XautoNameResolution -extension simpleMode.xml
	@# xjc CMMonitoring.xsd # cannot use this because of stupid infix notation... :-(
	(cd $(ORG) ; find . -name '*.java' | sed -e 's/^[.][/]//'>org-java-files)
	(cd $(ORG) ; javac --add-modules java.se.ee -d $(ORGBIN) @org-java-files)
	(cd $(ORGBIN) ; find . -name '*.class' > classes)
	(cd $(ORGBIN) ; jar cf $(BIN)/cumulus-xsd-schema.jar @classes)

$(BIN)/translator.jar: $(BIN)/Translator/Translate.class $(BIN)/Translator/SLAManager.class makefile
	(cd $(BIN) ; find . -name '*.class' > classes)
	(cd $(BIN) ; jar cfe translator.jar Translator.Translate @classes)

$(BIN)/Translator/Translate.class: Translator/Translate.java $(BIN)/cumulus-xsd-schema.jar makefile
	javac --add-modules java.se.ee -d $(BIN) -cp ${ABCL_ROOT}/abcl.jar:$(BIN)/cumulus-xsd-schema.jar Translator/Translate.java

$(BIN)/Translator/SLAManager.class: Translator/SLAManager.java $(BIN)/Translator/Translate.class $(BIN)/cumulus-xsd-schema.jar makefile
	javac --add-modules java.se.ee -d $(BIN) -cp $(BIN)/:${ABCL_ROOT}/abcl.jar:$(BIN)/cumulus-xsd-schema.jar Translator/SLAManager.java

$(BIN)/cumulus-translator.jar: $(BIN)/cumulus-xsd-schema.jar $(BIN)/translator.jar
	-rm -rf $(FBIN)
	-mkdir $(FBIN)
	(cd $(FBIN) ; unzip $(BIN)/cumulus-xsd-schema.jar ; yes | unzip $(BIN)/translator.jar ; find . -name '*.class' > classes ; jar cfe $(BIN)/cumulus-translator.jar Translator.Translate @classes)
	-rm -rf $(FBIN)

run: $(BIN)/cumulus-xsd-schema.jar $(BIN)/translator.jar
	@#java $(JFLAGS) -cp ${ABCL_ROOT}/abcl.jar:$(BIN)/cumulus-xsd-schema.jar:$(BIN)/translator.jar Translator.Translate $(CMS)/CM_Instance3.xml > ../Cms/sample1.prism 2> ../Cms/sample1.lisp
	java $(JFLAGS) -cp ${ABCL_ROOT}/abcl.jar:$(BIN)/cumulus-xsd-schema.jar:$(BIN)/translator.jar Translator.Translate $(CMS)/test-input.xml > ../Cms/sample1.prism 2> ../Cms/sample1.lisp

runm: $(BIN)/cumulus-xsd-schema.jar $(BIN)/translator.jar
	java $(JFLAGS) -cp ${ABCL_ROOT}/abcl.jar:$(BIN)/cumulus-xsd-schema.jar:$(BIN)/translator.jar Translator.SLAManager $(CMS)/z-test-input-for-validation.xml

run4: $(BIN)/cumulus-xsd-schema.jar $(BIN)/translator.jar
	java $(JFLAGS) -cp ${ABCL_ROOT}/abcl.jar:$(BIN)/cumulus-xsd-schema.jar:$(BIN)/translator.jar Translator.Translate $(CMS)/CM_Instance4.xml > ../Cms/sample1-4.prism 2> ../Cms/sample1-4.lisp

runfat: $(BIN)/cumulus-translator.jar
	java $(JFLAGS) -jar ${ABCL_ROOT}/abcl.jar:$(BIN)/cumulus-translator.jar $(CMS)/CM_Instance3.xml > ../Cms/sample1.prism 2> ../Cms/sample1.lisp

clean:
	-rm -rf $(ORG) $(BIN) $(FBIN) $(ORGBIN) org-java-files *~ */*~ $(CMS)/*~
	-mkdir $(BIN)
	-mkdir $(ORG)
	-mkdir $(FBIN)
	-mkdir $(ORGBIN)
