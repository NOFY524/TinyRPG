JAVAC      = javac
JAR        = jar

PLUGIN     = TinyRPG
SRC_DIR    = src
BUILD_DIR  = build
LIB_DIR    = lib

JAVA_SRCS  = $(shell find $(SRC_DIR) -name "*.java")
CLASS_FILES= $(patsubst $(SRC_DIR)/%.java,$(BUILD_DIR)/%.class,$(JAVA_SRCS))

JFLAGS     = -cp $(LIB_DIR)/*.jar -d $(BUILD_DIR) -sourcepath $(SRC_DIR)

.PHONY: all clean copy

all: $(PLUGIN).jar

$(PLUGIN).jar: $(CLASS_FILES) plugin.yml
	cp plugin.yml $(BUILD_DIR)/
	cd $(BUILD_DIR) && $(JAR) cf ../$(PLUGIN).jar .

$(BUILD_DIR)/%.class: $(SRC_DIR)/%.java
	mkdir -p $(dir $@)
	$(JAVAC) $(JFLAGS) $<

clean:
	rm -rf $(BUILD_DIR)
	rm -f $(PLUGIN).jar

copy: $(PLUGIN).jar
	cp $(PLUGIN).jar ./MCServer/plugins