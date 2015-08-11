package filter;

import filter.filter_exception;

public class iir{
	
	int P;	//feedforward(numerator) filter order
	int Q;	//feedback(denominator) filter order
	double[] filter_numerator_coefficients;		//b
	double[] filter_denominator_coefficients;	//a
	double[] past_input_values;
	double[] past_output_values;
	
	public iir(int P,int Q)
	{
		set_P(P);
		set_Q(Q);
	}
	
	public iir(double[] filter_numerator_coefficients,double[] filter_denominator_coefficients)
	{
		set_P(filter_numerator_coefficients.length - 1);
		set_Q(filter_denominator_coefficients.length - 1);
		
		use_as_numerator(filter_numerator_coefficients);
		use_as_denominator(filter_denominator_coefficients);
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

	public int get_Q() 
	{
		return this.Q;
	}

	public void set_Q(int q) 
	{
		this.Q = q;
		this.past_output_values=new double[Q];
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
	
	
	public void use_as_denominator(double[] filter_denominator)
	{
		try
		{
			if(filter_denominator.length != (this.Q+1))
			{
				throw new filter_exception("Dimension Mismatch. Length of the denominator coefficient array should be equal to Q.");
			}
		}
		catch(filter_exception fe)
		{
			fe.printStackTrace();
			System.exit(11);	//END PROGRAM WITH NON-ZERO VALUE TO INDICATE ERROR
		}
		this.filter_denominator_coefficients=filter_denominator;
	}

	public void update_past_input_values(double input_value)
	{
		for(int count_0=(this.P-1);count_0 > 0;count_0--)
		{
			this.past_input_values[count_0]=this.past_input_values[count_0-1];
		}
		
		this.past_input_values[0]=input_value;	
	}
	
	public void update_past_output_values(double output_value)
	{
		for(int count_0=(this.Q-1);count_0 > 0;count_0--)
		{
			this.past_output_values[count_0]=this.past_output_values[count_0-1];
		}
		
		this.past_output_values[0]=output_value;	
	}
	
	public double evaluate(double input_data)
	{
		double return_value=filter_numerator_coefficients[0]*input_data;
				
		for(int count_0=1;count_0<=this.P;count_0++)
		{
			return_value=return_value+filter_numerator_coefficients[count_0]*past_input_values[count_0-1];
		}
		
		for(int count_0=1;count_0<=this.Q;count_0++)
		{
			return_value=return_value-filter_denominator_coefficients[count_0]*past_output_values[count_0-1];
		}
		
		return_value=((return_value)/filter_denominator_coefficients[0]);
		
		update_past_input_values(input_data);
		update_past_output_values(return_value);
		
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
		double temp_2=0;
				
		for (int count_0=0;count_0<input_data.length;count_0++)
		{
			temp_1=0;
			temp_2=0;
					
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
			  
			if(count_0<this.Q)
			{
				for(int count_1=1;count_1<=count_0;count_1++)
				{
					temp_2=temp_2+filter_denominator_coefficients[count_1]*output_data[count_0-(count_1)];		 
				}			  
			}
			else
			{
				for(int count_1=1;count_1<=this.Q;count_1++)
				{
					temp_2=temp_2+filter_denominator_coefficients[count_1]*output_data[count_0-(count_1)];
				} 
			}
			  
			output_data[count_0]=(temp_1-temp_2)/filter_denominator_coefficients[0];
		}
		
		return output_data;
	}
		
}