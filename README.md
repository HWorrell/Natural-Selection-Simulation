# Natural-Selection-Simulation
A Simulation of the prevalence of Sickle-Cell Genes in the presence and absence of Malaria
This program simulates a series of generations of individuals, some carrying the Allele for Sickle-Cell, some not.
The program asks 7-8 questions to determine the parameters to be used in the simulation, then runs.


Available Parameters:

Initial Population Size

Maximum number of children each "couple" is permitted

Presence or abscense of Malaria

Probablity of death for a Heterozygous Sickle-Cell carrier

Probability of each allele in the initial population being a sickle-cell allele

Probability of a Homozygous non-sickle-cell individual dying from malaria.  Sickle-Cell Heterozygous individuals have only 25% of this risk.

Maximum number of generations to run.

On termination of the run, a report is generated, allowing in-depth comparison between generations.

The run can terminate in four ways:
1) User termination.
2) Reaching Pre-Determined number of Generations
3) Extinction of the Sickle-Cell allele
4) Extinction of the Human Race

User Termination may occur due to running time.  The complexity for the creation of each new generation is approximatly O(n^3), leading to very log calculation times if the population grows to very large numbers.

This simulation is not designed to provide a detailed view of natural selection.  Rather, it is intended to be utilized in a classroom environment, to allow instructors to show generailzed trends within Natural Selection, including the fact that "fitness" is defined by the environment of the population, rather than arbitrary values.
