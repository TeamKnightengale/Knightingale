package edu.allegheny.TweetAnalyze.UI;
import java.io.File;
import com.beust.jcommander.*;

public class FileConverter implements IStringConverter<File> {
  @Override
  public File convert(String value) {
    return new File(value);
  }
}
