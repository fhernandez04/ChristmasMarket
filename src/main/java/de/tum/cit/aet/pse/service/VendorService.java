package main.java.de.tum.cit.aet.pse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.de.tum.cit.aet.pse.entity.Vendor;
import main.java.de.tum.cit.aet.pse.repository.VendorRepository;

@Service
public class VendorService {
    
    @Autowired
    private VendorRepository vendorRepository;

    public Vendor save(Vendor newVendor) {
		return vendorRepository.save(newVendor);
	}

	public Iterable<Vendor> findAll() {
		return vendorRepository.findAll();
	}

	public Vendor findById(int VendorId) {
		return vendorRepository.findById(VendorId).orElse(null);
	}

	public void deleteById(int VendorId) {
		try {
			vendorRepository.deleteById(VendorId);
		}
		catch (Exception e) {
			System.err.println("Unable to delete Vendor with ID: " + VendorId);
		}
	}
}
