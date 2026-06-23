import React from 'react';
import './RangeSlider.css';

interface RangeSliderProps {
  min: number;
  max: number;
  value: number;
  onChange: (value: number) => void;
  label?: string;
}

export const RangeSlider: React.FC<RangeSliderProps> = ({
  min,
  max,
  value,
  onChange,
  label,
}) => {
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    onChange(Number(e.target.value));
  };

  const percentage = ((value - min) / (max - min)) * 100;

  return (
    <div className="range-slider">
      {label && <label className="range-slider__label">{label}</label>}
      <div className="range-slider__track">
        <div
          className="range-slider__filled"
          style={{ width: `${percentage}%` }}
        />
        <input
          type="range"
          min={min}
          max={max}
          value={value}
          onChange={handleChange}
          className="range-slider__input"
        />
        <div
          className="range-slider__thumb"
          style={{ left: `${percentage}%` }}
        />
      </div>
      <div className="range-slider__values">
        <span>{min.toLocaleString()}</span>
        <span>{max.toLocaleString()}</span>
      </div>
    </div>
  );
};