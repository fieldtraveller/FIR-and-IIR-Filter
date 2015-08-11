package filter;

import filter.filter_exception;

public class fir{
	
	int P;	//feedforward(numerator) filter order
	double[] filter_numerator_coefficients;		//b
	double[] past_input_values;
	
	public fir(int P)
	{
		set_P(P);
	}
	
	public fir(double[] filter_numerator_coefficients)
	{
		set_P(filter_numerator_coefficients.length - 1);
		
		use_as_numerator(filter_numerator_coefficients);
	}
	
	public int get_P() 
	{
		return this.P;
	}

	public void set_P(int p) 
	{
		this.P = p;
		this.past_input_values=new double[P];
	}
	
	public void use_as_numerator(double[] filter_numerator)
	{
		try
		{
			if(filter_numerator.length != (this.P+1))
			{
				throw new filter_exception("Dimension Mismatch. Length of the numerator coefficient array should be equal to P.");
			}
		}
		catch(filter_exception fe)
		{
			fe.printStackTrace();
			System.exit(15);	//END PROGRAM WITH NON-ZERO VALUE TO INDICATE ERROR
		}
		
		this.filter_numerator_coefficients=filter_numerator;
	}

	public void update_past_input_values(double input_value)
	{
		for(int count_0=(this.P-1);count_0 > 0;count_0--)
		{
			this.past_input_values[count_0]=this.past_input_values[count_0-1];
		}
		
		this.past_input_values[0]=input_value;	
	}
	
	public double evaluate(double input_data)
	{
		double return_value=filter_numerator_coefficients[0]*input_data;
				
		for(int count_0=1;count_0<=this.P;count_0++)
		{
			return_value=return_value+filter_numerator_coefficients[count_0]*past_input_values[count_0-1];
		}
		
		update_past_input_values(input_data);
		
		return return_value;
	}
	
	public double[] evaluate_array(double[] input_data)
	{
		double[] output_data=new double[input_data.length];
		
		for (int count_0=0;count_0<input_data.length;count_0++)
		{
			output_data[count_0]=evaluate(input_data[count_0]);
		}
		
		return output_data;
	}
	
	public double[] evaluate_array_no_history(double[] input_data)
	{
		double[] output_data=new double[input_data.length];
		double temp_1=0;
				
		for (int count_0=0;count_0<input_data.length;count_0++)
		{
			temp_1=0;
					
			if(count_0 < this.P)
			{
				for(int count_1=0;count_1<=count_0;count_1++)
				{
					temp_1=temp_1+filter_numerator_coefficients[count_1]*input_data[count_0-(count_1)];
				}		     			  
			}
			else
			{	
				for(int count_1=0;count_1<=this.P;count_1++)
				{
					temp_1=temp_1+filter_numerator_coefficients[count_1]*input_data[count_0-(count_1)];
				}
			}
			  
			output_data[count_0]=temp_1;
		}
		
		return output_data;
	}
		
}