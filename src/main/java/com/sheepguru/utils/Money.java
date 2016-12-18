
package com.sheepguru.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;
import java.text.NumberFormat;
import java.util.ArrayList;
import com.sheepguru.utils.HashCodeUtil;
import java.util.Collection;



/**
 * This makes money work properly.
 * As a bonus, I'm fairly certain this is thread safe.
 * 
 * @author John Quinn
 */
public class Money implements Comparable<Money>
{
  /**
   * Default rounding mode is HALF_UP
   */
  public static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_UP;

  /**
   * Default Language used for locale
   */
  public static final String DEFAULT_LANGUAGE = "en";

  /**
   * Default country code for locale 
   */
  public static final String DEFAULT_COUNTRY = "US";
  
  /**
   * Internal amount 
   */
  private final BigDecimal fAmount;

  /**
   * Language and Country as a Locale
   */
  private final Locale fLocale;

  /**
   * Currency 
   */
  private final Currency fCurrency;

  /**
   * Rounding Mode 
   */
  private final RoundingMode fMode;// = DEFAULT_ROUNDING;

  
  /**
   * Create a new default money instance with a zero value using default rounding 
   */
  public Money()
  {
    fLocale = new Locale( DEFAULT_LANGUAGE, DEFAULT_COUNTRY );
    fCurrency = Currency.getInstance( fLocale );
    fMode = DEFAULT_ROUNDING;
    fAmount = BigDecimal.ZERO.setScale( getDecimals(), fMode );
    checkAmount();
  }


  /**
   * Create a new money instance with custom currencies and rounding mode
   * @param aAmount Amount
   * @param locale The locale
   * @param aCurrency Currency
   * @param aMode Rounding Mode 
   */
  public Money( BigDecimal aAmount, Locale locale, Currency aCurrency, RoundingMode aMode )
  {
    fLocale = locale;
    fCurrency = aCurrency;
    fMode = aMode;
    fAmount = aAmount.setScale( getDecimals(), fMode );
    checkAmount();
  }


  /**
   * Create a new money instance with custom currencies and rounding mode
   * @param aAmount Amount
   * @param aCurrency Currency
   */
  public Money( BigDecimal aAmount, Currency aCurrency )
  {
    this( aAmount, new Locale( DEFAULT_LANGUAGE, DEFAULT_COUNTRY ), aCurrency, DEFAULT_ROUNDING );
  }


  /**
   * Create a new money instance with an amount using default currency and rounding mode
   * @param aAmount Amount 
   */
  public Money( BigDecimal aAmount )
  {
    fLocale = new Locale( DEFAULT_LANGUAGE, DEFAULT_COUNTRY );
    fCurrency = Currency.getInstance( fLocale );
    fMode = DEFAULT_ROUNDING;
    if ( !( aAmount instanceof BigDecimal ))
      fAmount = BigDecimal.ZERO.setScale( getDecimals(), fMode );
    else
      fAmount = aAmount.setScale( getDecimals(), fMode );
    
    checkAmount();
  }



  /**
   * Create a new money instance
   * @param aAmount Amount 
   */
  public Money( int aAmount )
  {
    this( new BigDecimal( aAmount ));
  }


  /**
   * Create a new money instance
   * @param aAmount Amount
   */
  public Money( String aAmount )
  {
    this( new BigDecimal( aAmount ));
  }


  /**
   * Create a new money instance
   * @param aAmount Amount
   */
  public Money( double aAmount )
  {
    this( new BigDecimal( aAmount ));
  }


  /**
   * Check to see if currencies match
   * @param aThat Money to check
   * @return if currencies match 
   */
  public boolean currencyMatch( Money aThat )
  {
    if ( aThat != null )
      return fCurrency.equals( aThat.getCurrency());
    else
      return false;
  }


  /**
   * Check to see if money is positive
   * @return is positive
   */
  public boolean isPositive()
  {
    return fAmount.compareTo( BigDecimal.ZERO ) > 0;
  }


  /**
   * Check to see if money is negative
   * @return is negative 
   */
  public boolean isNegative()
  {
    return fAmount.compareTo( BigDecimal.ZERO ) < 0;
  }


  /**
   * Check to see of money is zero
   * @return is zero 
   */
  public boolean isEmpty()
  {
    return fAmount.compareTo( BigDecimal.ZERO ) == 0;
  }
 

  /**
   * Add 2 monies together
   * @param m Money
   * @return Money + money 
   * @throws CurrencyMismatchException if currencies don't match 
   */
  public Money plus( Money m ) throws CurrencyMismatchException 
  {
    checkCurrencyMatch( m );
    return new Money( fAmount.add( m.asBigDecimal()), fLocale, fCurrency, fMode );
  }


  /**
   * Add a collection of moneys together 
   * @param cm Money collection
   * @return Sum of money in collection 
   * @throws CurrencyMismatchException if currencies don't match 
   */
  public Money sum( Collection<Money> cm ) throws CurrencyMismatchException
  {
    Money m = new Money( BigDecimal.ZERO, fLocale, fCurrency, fMode );

    if ( cm.isEmpty()) return new Money();

    for ( Money mo : cm )
      m = m.plus( mo );

    return m;
  }


  /**
   * Subtract money from money 
   * @param m Amount to subtract
   * @return new amount 
   * @throws CurrencyMismatchException
   */
  public Money minus( Money m ) throws CurrencyMismatchException
  {
    checkCurrencyMatch( m );
    return new Money( fAmount.subtract( m.asBigDecimal()), fLocale, fCurrency, fMode );
  }


  public Money times( double factor )
  {
    BigDecimal f = new BigDecimal( factor );
    return new Money( fAmount.multiply( f ), fLocale, fCurrency, fMode );
  }


  public Money times( int factor ) 
  {
    BigDecimal f = new BigDecimal( factor );
    return new Money( fAmount.multiply( f ), fLocale, fCurrency, fMode );
  }


  public Money times( Money factor ) throws CurrencyMismatchException 
  {
    checkCurrencyMatch( factor );
    Money m = new Money( fAmount.multiply( factor.asBigDecimal()), fLocale, fCurrency, fMode );
    return m;
  }


  public Money div( int divisor ) 
  {
    BigDecimal d = new BigDecimal( divisor );
    return new Money( fAmount.divide( d ), fLocale, fCurrency, fMode );
  }


  public Money div( double divisor ) 
  {
    BigDecimal d = new BigDecimal( divisor );
    return new Money( fAmount.divide( d ), fLocale, fCurrency, fMode );
  }


  public Money div( Money divisor )
  {
    return new Money( fAmount.divide( divisor.asBigDecimal()), fLocale, fCurrency, fMode );
  }


  public int mod( int divisor )
  {
    BigDecimal d = new BigDecimal( divisor );
    return fAmount.remainder( d ).intValue();
  }




  public Money abs() 
  {
    if ( isNegative())
      return negate();
    else
      return this;
  }


  public Money negate()
  {
    return times( -1 );
  }

  


  



  public Currency getCurrency()
  {
    return fCurrency;
  }


  public Locale getLocale()
  {
    return fLocale;
  }


  public RoundingMode getRoundingMode()
  {
    return fMode;
  }


  public BigDecimal asBigDecimal()
  {
    return fAmount;
  }
  
  
  public float floatValue()
  {
    return fAmount.floatValue();    
  }


  public String toCurrencyString()
  {
    return NumberFormat.getCurrencyInstance( fLocale ).format( fAmount.doubleValue());
  }


  public String toPercentString()
  {
    return NumberFormat.getPercentInstance( fLocale ).format( fAmount.doubleValue());
  }


  @Override 
  public String toString()
  {
    return fAmount.toPlainString();    
  }


  /**
   * Return a list of valid locales 
   * @return Valid locale list 
   */
  public Locale[] getValidLocales()
  {
    Locale[] locales = NumberFormat.getAvailableLocales();
    ArrayList<Locale> out = new ArrayList();

    
    for ( Locale l : locales )
    {
      if (( l.getLanguage().isEmpty()) || ( l.getCountry().isEmpty()))
        continue;
      out.add( l );
    }

    Locale[] outl = new Locale[out.size()];
    return out.toArray( outl );
  }

  

  /**
   * Return a list of valid locales by string representation 
   * @return Locale string list 
   */
  public String[] getValidLocaleStrings()
  {
    Locale[] locales = getValidLocales();
    String[] out = new String[locales.length];
    
    for ( int i = 0; i < locales.length; i++ )
      out[i] = locales[i].toString();

    return out;
  }



  /**
   * Equals (insensitive to scale).
   *
   * <P>Return <tt>true</tt> only if the amounts are equal.
   * Currencies must match.
   * This method is <em>not</em> synonymous with the <tt>equals</tt> method.
   */
  public boolean equals(Money aThat)
  {
    checkCurrencyMatch( aThat );
    return compareAmount(aThat) == 0;
  }
  

  /**
  * Greater than.
  *
  * <P>Return <tt>true</tt> only if  'this' amount is greater than
  * 'that' amount. Currencies must match.
  */
  public boolean greaterThan( Money aThat )
  {
    checkCurrencyMatch(aThat);
    return compareAmount(aThat) > 0;
  }


  public boolean greaterThanZero()
  {
    return isPositive();
    //return ( fAmount.intValue() > 0 );
  }

  public boolean greaterThanEqualToZero()
  {
    return ( fAmount.intValue() >= 0 );
  }


  public boolean lessThanZero()
  {
    return isNegative();
    //return ( fAmount.intValue() < 0 );
  }


  public boolean lessThanEqualToZero()
  {
    return ( fAmount.intValue() <= 0 );
  }


  /**
  * Greater than or equal to.
  *
  * <P>Return <tt>true</tt> only if 'this' amount is
  * greater than or equal to 'that' amount. Currencies must match.
  */
  public boolean greaterThanOrEqualTo( Money aThat )
  {
    checkCurrencyMatch(aThat);
    return compareAmount(aThat) >= 0;
  }

  
  /**
  * Less than.
  *
  * <P>Return <tt>true</tt> only if 'this' amount is less than
  * 'that' amount. Currencies must match.
  */
  public boolean lessThan( Money aThat )
  {
    checkCurrencyMatch(aThat);
    return compareAmount(aThat) < 0;
  }

  
  /**
   * Less than or equal to.
   *
   * <P>Return <tt>true</tt> only if 'this' amount is less than or equal to
   * 'that' amount. Currencies must match.
   */
  public boolean lessThanOrEqualTo( Money aThat )
  {
    checkCurrencyMatch(aThat);
    return compareAmount(aThat) <= 0;
  }


  /**
   * Retrieve the exact value as an integer.
   * This is good for when the money is represented by an int. 
   * @return int val 
   */
  public int intVal()
  {
    return fAmount.setScale( 0, fMode ).intValue();
  }
  

  /**
   * Multiply the current amount by 100 and return the integer value 
   * @return amount * 100 
   */
  public int asDBInteger()
  {
    return times( 100 ).asBigDecimal().intValue();
  }


  private int compareAmount(Money aThat)
  {
    return fAmount.compareTo( aThat.asBigDecimal());
  }


  /**
   * Checks a locale to determine if it is valid or not 
   * @param l Locale to check
   * @return boolean Is valid
   */
  private boolean checkLocale( Locale l )
  {
    for ( Locale locale : NumberFormat.getAvailableLocales())
    {
      if ( l.equals( locale ))
        return true;
    }

    return false;
  }


  /**
   * Return the number of decimals used in the current currency 
   * @return
   */
  private int getDecimals()
  {
    return fCurrency.getDefaultFractionDigits();
  }


  /**
   * Check the amount 
   * @throws IllegalArgumentException
   */
  private void checkAmount() throws IllegalArgumentException 
  {
    if( fAmount == null ) {
      throw new IllegalArgumentException( "Amount cannot be null" );
    }
    
    if( fCurrency == null ) {
      throw new IllegalArgumentException( "Currency cannot be null" );
    }

    if ( fAmount.scale() > getDecimals()) {
      throw new IllegalArgumentException(
        "Number of decimals is " + fAmount.scale() + ", but currency only takes " +
        getDecimals() + " decimals."
      );
    }
  }


  private void checkCurrencyMatch( Money aThat ) throws CurrencyMismatchException
  {
    if ( !fCurrency.equals( aThat.getCurrency()))
      throw new CurrencyMismatchException( aThat.getCurrency() + " does not match the expected currency " + fCurrency );
  }


  @Override
  public boolean equals( Object aThat )
  {
    if ( this == aThat ) return true;

    if ( !( aThat instanceof Money )) return false;

    Money that = (Money)aThat;

    return (( fAmount.equals( that.asBigDecimal())) &&
            ( fCurrency.equals( that.getCurrency())) &&
            ( fMode.equals( that.getRoundingMode())));
  }


  @Override
  public int hashCode()
  {
    int res = HashCodeUtil.SEED;
    HashCodeUtil.hash( res, fAmount );
    HashCodeUtil.hash( res, fCurrency );
    HashCodeUtil.hash( res, fMode );

    return res;
  }


  @Override
  public int compareTo( Money aThat )
  {
    final int EQUAL = 0;

    if ( this == aThat ) return EQUAL;

    int c = fAmount.compareTo( aThat.asBigDecimal());
    if ( c != EQUAL ) return c;

    c = fCurrency.getCurrencyCode().compareTo( aThat.getCurrency().getCurrencyCode());
    if ( c != EQUAL ) return c;

    c = fMode.compareTo( aThat.getRoundingMode());
    if ( c != EQUAL ) return c;

    return EQUAL;
  }


}
